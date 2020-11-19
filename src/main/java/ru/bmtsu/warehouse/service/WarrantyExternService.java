package ru.bmtsu.warehouse.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import ru.bmtsu.warehouse.exception.NotFoundWarrantyException;
import ru.bmtsu.warehouse.exception.WarrantyServiceNotAvailableException;
import ru.bmtsu.warehouse.service.dto.WarrantyRequestDTO;
import ru.bmtsu.warehouse.service.dto.WarrantyResponseDTO;

import java.net.URI;
import java.util.UUID;

@Service
@Slf4j
public class WarrantyExternService {
    private final RestTemplate client;
    private final String host;
    private final String path;
    private final String warranty;

    @Autowired
    public WarrantyExternService(RestTemplate client,
                                 @Value("${service.warranty.host}") String host,
                                 @Value("${service.warranty.path}") String path,
                                 @Value("${service.warranty.check-warranty}") String warranty) {
        this.client = client;
        this.host = host;
        this.path = path;
        this.warranty = warranty;
    }

    public WarrantyResponseDTO getWarrantyInformation(UUID itemUid, WarrantyRequestDTO warrantyRequest) {
        URI uri = UriComponentsBuilder.fromUriString(host)
                .path(path + "/" + itemUid.toString() + warranty)
                .build()
                .encode()
                .toUri();

        ResponseEntity<WarrantyResponseDTO> response = null;
        try {
            response = client.postForEntity(uri, warrantyRequest, WarrantyResponseDTO.class);
            if (response.getStatusCode() != HttpStatus.OK) { throw new WarrantyServiceNotAvailableException(); }
        } catch (HttpClientErrorException e) {
            if (e.getStatusCode().equals(HttpStatus.NOT_FOUND)) {
                throw new NotFoundWarrantyException();
            } else {
                throw new WarrantyServiceNotAvailableException();
            }
        } catch (Exception e) {
            throw new WarrantyServiceNotAvailableException();
        }

        return response.getBody();
    }
}
