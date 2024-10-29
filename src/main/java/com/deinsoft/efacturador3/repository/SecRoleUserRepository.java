/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.deinsoft.efacturador3.repository;

import com.deinsoft.efacturador3.model.Parametro;
import com.deinsoft.efacturador3.model.SecRole;
import com.deinsoft.efacturador3.model.SecRoleUser;
import com.deinsoft.efacturador3.model.SecUser;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 *
 * @author EDWARD-PC
 */
public interface SecRoleUserRepository  extends JpaRepository<SecRoleUser,Long>{
   List<SecRoleUser> findBySecUserId(long userId);
}
