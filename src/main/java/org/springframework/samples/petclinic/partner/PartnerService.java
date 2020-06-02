package org.springframework.samples.petclinic.partner;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.samples.petclinic.order.PaymentService;
import org.springframework.samples.petclinic.order.repository.AccountWireCardRepository;
import org.springframework.samples.petclinic.partner.model.ActivityBranch;
import org.springframework.samples.petclinic.partner.model.Address;
import org.springframework.samples.petclinic.partner.model.Partner;
import org.springframework.samples.petclinic.partner.model.PartnerAddress;
import org.springframework.samples.petclinic.partner.model.PartnerContact;
import org.springframework.samples.petclinic.partner.model.Plan;
import org.springframework.samples.petclinic.partner.repository.ActivityBranchRepository;
import org.springframework.samples.petclinic.partner.repository.PartnerRepository;
import org.springframework.samples.petclinic.partner.repository.PlanRepository;
import org.springframework.samples.petclinic.partner.request.AddressRequest;
import org.springframework.samples.petclinic.partner.request.CreatePlanRequest;
import org.springframework.samples.petclinic.partner.request.PartnerRequest;
import org.springframework.samples.petclinic.partner.request.SignupRequest;
import org.springframework.samples.petclinic.partner.response.PartnerResponse;
import org.springframework.samples.petclinic.partner.response.PlanResponse;
import org.springframework.samples.petclinic.system.exception.CustomGenericNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

@Service
@Transactional
public class PartnerService {

	private static final Log LOGGER = LogFactory.getLog(PartnerService.class);

	private static final String API_KEY = "b47301eD474e48c9428f4Rc";

	@Autowired
	private PartnerRepository partnerRepository;

	@Autowired
	private PlanRepository planRepository;

	@Autowired
	private AccountWireCardRepository accountWireCardRepository;

	@Autowired
	private PartnerAccountService partnerAccountService;

	@Autowired
	private PaymentService paymentService;

	@Autowired
	private ActivityBranchRepository activityBranchRepository;

	public PartnerResponse createPartner(PartnerRequest partnerRequest) {
		Partner partner = this.convertToPartner(partnerRequest);
		return convertToPartnerResponse(partner);
	}

	public List<PartnerResponse> findAllPartners() {
		List<Partner> partners = (List<Partner>) partnerRepository.findAll();
		return partners.stream().sorted(Comparator.comparing(Partner::getCreateDate).reversed())
				.map(this::convertToPartnerResponse).collect(Collectors.toList());
	}

	public PartnerResponse findPartnerById(Long partnerId) {
		Partner partner = partnerRepository.findById(partnerId)
				.orElseThrow(() -> new CustomGenericNotFoundException("Error: Partner is not found."));
		return convertToPartnerResponse(partner);
	}

	public void deleteById(Long partnerId) {
		Partner partner = partnerRepository.findById(partnerId)
				.orElseThrow(() -> new CustomGenericNotFoundException("Error: Partner is not found."));
		partnerRepository.delete(partner);
	}

	public PartnerResponse findPartnerByIdUser(String userId) {
		Partner partner = partnerRepository.findByUserId(userId).orElse(null);
		if (partner == null) {
			return null;
		}
		return convertToPartnerResponse(partner);
	}

	public void createPlan(CreatePlanRequest createPlanRequest, Long partnerId, Long planId) {

		Partner partner = partnerRepository.findById(partnerId)
				.orElseThrow(() -> new CustomGenericNotFoundException("Error: Partner is not found."));
		
		Plan plan = planRepository.findById(planId)
				.orElseThrow(() -> new CustomGenericNotFoundException("Error: Plan is not found."));

		partner.setPlan(plan);
		PartnerAccount partnerAccount = partnerAccountService.createPartnerAccount(partnerId);

		paymentService.accountWireCard(partner, plan, partnerAccount, createPlanRequest);
	}

	public List<PlanResponse> findAllPlansActive() {
		List<Plan> plans = (List<Plan>) planRepository.findAll();
		return plans.stream().filter(x -> x.getActive()).map(this::convertToPlanResponse).collect(Collectors.toList());
	}

	public void importPartner() {
		SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
		final String url = "https://tpkmarket.eybpro.com/api/get_clientes.php?apikey=" + API_KEY;
		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));

		RestTemplate restTemplate = new RestTemplate();
		ResponseEntity<Partners[]> response = restTemplate.getForEntity(url, Partners[].class);
		Partners[] partners = response.getBody();

		for (Partners p : partners) {

			// create user
			String userId = this.callUser(p);

			Date data = null;
			try {
				data = format.parse(p.getDatacadastro());
			} catch (ParseException e) {
				throw new CustomGenericNotFoundException("Formato de data inválido");
			}
			Partner partner = new Partner();
			partner.setDeltaId(p.getId());
			partner.setDeltaDate(data);
			partner.setDocument(p.getCnpj());
			partner.setFantasia(p.getFantasia());
			partner.setRazaoSocial(p.getRazaosocial());
			partner.setUserId(userId);
			PartnerContact pc = new PartnerContact();
			pc.setDocument(p.getCpf());
			pc.setName(p.getContato());
			pc.setEmail(p.getEmail());
			List<PartnerContact> contacts = new ArrayList<PartnerContact>();
			contacts.add(pc);
			partner.setContacts(contacts);

			PartnerAddress pa = new PartnerAddress();
			pa.setCity(p.getCidade());
			pa.setComplement(p.getEnderecocomplemento());
			pa.setCountry("Brasil");
			pa.setNeighborhood(p.getBairro());
			pa.setNumber(p.getEndereconumero());
			pa.setProvince(p.getUf());
			pa.setReferencePoint(null);
			pa.setDefaults(true);
			pa.setStreet(p.getEndereco());
			pa.setZip(p.getCep());

			AddressRequest ar = this.callAddress(pa);
			pa.setLat(new Float(ar.getAdresses().stream().findFirst().get().getLatitude()));
			pa.setLng(new Float(ar.getAdresses().stream().findFirst().get().getLongitude()));

			List<PartnerAddress> addresses = new ArrayList<PartnerAddress>();
			addresses.add(pa);
			partner.setAdresses(addresses);

			ActivityBranch activityBranch = activityBranchRepository.findById(new Long(p.getRamoatividade()))
					.orElseThrow(() -> new CustomGenericNotFoundException("Error: Activity Branch is not found."));
			partner.setActivityBranch(activityBranch);
			partnerRepository.save(partner);
		}
	}

	private AddressRequest callAddress(PartnerAddress pa) {
		final String uri = "http://localhost:8089/geocodes";

		List<Address> adresses = new ArrayList<Address>();

		AddressRequest ar = new AddressRequest();
		Address a = new Address();
		a.setCity(pa.getCity());
		a.setComplement(pa.getComplement());
		a.setCountry(pa.getCountry());
		a.setNeighborhood(pa.getNeighborhood());
		a.setNumber(pa.getNumber());
		a.setProvince(pa.getProvince());
		a.setStreet(pa.getStreet());
		a.setZip(pa.getZip());
		adresses.add(a);
		ar.setAdresses(adresses);

		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
		HttpEntity<AddressRequest> entity = new HttpEntity<AddressRequest>(ar, headers);
		RestTemplate restTemplate = new RestTemplate();
		ResponseEntity<AddressRequest> result = restTemplate.postForEntity(uri, entity, AddressRequest.class);

		return result.getBody();
	}

	private String callUser(Partners p) {
		final String uri = "http://localhost:8088/api/auth/";
		SignupRequest signupRequest = new SignupRequest();
		signupRequest.setUsername(p.getFantasia());
		signupRequest.setEmail(p.getEmail());
		signupRequest.setPhone(p.getTelefoneprincipal());
		signupRequest.setPassword("123456");
		signupRequest.setBirthday(new Date());
		Set<String> role = new HashSet<String>();
		role.add("ROLE_PARTNER");
		signupRequest.setRole(role);
		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
		HttpEntity<SignupRequest> entity = new HttpEntity<SignupRequest>(signupRequest, headers);
		RestTemplate restTemplate = new RestTemplate();
		ResponseEntity<String> result = restTemplate.postForEntity(uri, entity, String.class);
		return result.getBody();
	}

	private PartnerResponse convertToPartnerResponse(Partner partner) {
		ModelMapper modelMapper = new ModelMapper();
		return modelMapper.map(partner, PartnerResponse.class);
	}

	private Partner convertToPartner(PartnerRequest partnerRequest) {
		ModelMapper modelMapper = new ModelMapper();
		return modelMapper.map(partnerRequest, Partner.class);
	}

	private PlanResponse convertToPlanResponse(Plan plan) {
		ModelMapper modelMapper = new ModelMapper();
		return modelMapper.map(plan, PlanResponse.class);
	}

}
