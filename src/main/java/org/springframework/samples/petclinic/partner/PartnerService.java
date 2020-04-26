package org.springframework.samples.petclinic.partner;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.system.CustomGenericNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class PartnerService {

	private static final Log LOGGER = LogFactory.getLog(PartnerService.class);

	@Autowired
	private PartnerRepository partnerRepository;
	
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
	

	private PartnerResponse convertToPartnerResponse(Partner partner) {
		ModelMapper modelMapper = new ModelMapper();
		return modelMapper.map(partner, PartnerResponse.class);
	}
	
	private Partner convertToPartner(PartnerRequest partnerRequest) {
		ModelMapper modelMapper = new ModelMapper();
		return modelMapper.map(partnerRequest, Partner.class);
	}
	
}
