/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.deinsoft.efacturador3.service;

import com.deinsoft.efacturador3.model.SecRoleUser;
import com.deinsoft.efacturador3.model.SecUser;
import java.util.HashMap;
import java.util.List;

/**
 *
 * @author EDWARD-PC
 */
public interface SecUserService {
    
    public SecUser getSecUserById(long id) ;
    public List<SecUser> getSecUsers();
    public SecUser save(SecUser empresa);
    public SecUser getSecUserByName(String name) ;
    public List<SecRoleUser> getSecRoleUserById(long userId);
}
