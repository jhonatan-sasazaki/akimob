package br.com.akrasia.akimob.address;

import org.springframework.stereotype.Service;

import br.com.akrasia.akimob.address.dtos.AddressCreateDTO;
import br.com.akrasia.akimob.address.dtos.AdministrativeAreaDTO;
import br.com.akrasia.akimob.address.dtos.CountryDTO;
import br.com.akrasia.akimob.address.dtos.LocalityDTO;
import br.com.akrasia.akimob.address.dtos.StreetDTO;
import br.com.akrasia.akimob.address.entities.Address;
import br.com.akrasia.akimob.address.entities.AdministrativeArea;
import br.com.akrasia.akimob.address.entities.Country;
import br.com.akrasia.akimob.address.entities.Locality;
import br.com.akrasia.akimob.address.entities.Street;
import br.com.akrasia.akimob.address.repositories.AddressRepository;
import br.com.akrasia.akimob.address.repositories.AdministrativeAreaRepository;
import br.com.akrasia.akimob.address.repositories.CountryRepository;
import br.com.akrasia.akimob.address.repositories.LocalityRepository;
import br.com.akrasia.akimob.address.repositories.StreetRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AddressService {

    private final AddressRepository addressRepository;
    private final StreetRepository streetRepository;
    private final LocalityRepository localityRepository;
    private final AdministrativeAreaRepository administrativeAreaRepository;
    private final CountryRepository countryRepository;

    public Address findOrCreateAddress(AddressCreateDTO addressCreateDTO) {
        if (addressCreateDTO.getId() != null) {
            return addressRepository.getReferenceById(addressCreateDTO.getId());
        }
        return createAddress(addressCreateDTO);

    }

    private Address createAddress(AddressCreateDTO addressCreateDTO) {
        Address address = new Address();
        address.setStreet(findOrCreateStreet(addressCreateDTO.getStreet()));
        address.setAddressNumber(addressCreateDTO.getAddressNumber());
        address.setComplement(addressCreateDTO.getComplement());
        address.setPostalCode(addressCreateDTO.getPostalCode());

        return address;
    }

    private Street findOrCreateStreet(StreetDTO streetDTO) {
        if (streetDTO.getId() != null) {
            return streetRepository.getReferenceById(streetDTO.getId());
        }
        return createStreet(streetDTO);
    }

    private Street createStreet(StreetDTO streetDTO) {
        Street street = new Street();
        street.setLocality(findOrCreateLocality(streetDTO.getLocality()));
        street.setName(streetDTO.getName());
        return street;
    }

    private Locality findOrCreateLocality(LocalityDTO localityDTO) {
        if (localityDTO.getId() != null) {
            return localityRepository.getReferenceById(localityDTO.getId());
        }
        return createLocality(localityDTO);
    }

    private Locality createLocality(LocalityDTO localityDTO) {
        Locality locality = new Locality();
        locality.setAdministrativeArea(findOrCreateAdministrativeArea(localityDTO.getAdministrativeArea()));
        locality.setName(localityDTO.getName());
        return locality;
    }

    private AdministrativeArea findOrCreateAdministrativeArea(AdministrativeAreaDTO administrativeAreaDTO) {
        if (administrativeAreaDTO.getId() != null) {
            return administrativeAreaRepository.getReferenceById(administrativeAreaDTO.getId());
        }
        return createAdministrativeArea(administrativeAreaDTO);
    }

    private AdministrativeArea createAdministrativeArea(AdministrativeAreaDTO administrativeAreaDTO) {
        AdministrativeArea administrativeArea = new AdministrativeArea();
        administrativeArea.setCountry(findOrCreateCountry(administrativeAreaDTO.getCountry()));
        administrativeArea.setName(administrativeAreaDTO.getName());
        return administrativeArea;
    }

    private Country findOrCreateCountry(CountryDTO countryDTO) {
        if (countryDTO.getId() != null) {
            return countryRepository.getReferenceById(countryDTO.getId());
        }
        return createCountry(countryDTO);
    }

    private Country createCountry(CountryDTO countryDTO) {
        Country country = new Country();
        country.setId(countryDTO.getIsoNumeric());
        country.setIso2(countryDTO.getIso2());
        country.setName(countryDTO.getName());
        return country;
    }

}
