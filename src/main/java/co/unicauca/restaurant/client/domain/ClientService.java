/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.unicauca.restaurant.client.domain;

import co.unicauca.restaurant.client.access.IClientAccess;

/**
 * Servicios que el cliente podrá usar del respectivo servidor
 *
 * @author braia
 */
public class ClientService {
    
    private final IClientAccess service;
    
    /**
     * Inyección de dependencia
     * @param service implementa la interfaz de acce
     */
    public ClientService(IClientAccess service){
        this.service = service;
    }
    
    
    
}
