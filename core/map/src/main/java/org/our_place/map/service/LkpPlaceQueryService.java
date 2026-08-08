package org.our_place.map.service;


import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

/**
 * Servicio de solo lectura para catalogos (lookups).
 * Los datos son estaticos/poco cambiantes, por eso se cachean.
 */
@Service
@RequiredArgsConstructor
public class LkpPlaceQueryService {

//

}

