package mathes.nametala.cadernetaapi.services.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import mathes.nametala.cadernetaapi.model.entitys.RoleEntity;
import mathes.nametala.cadernetaapi.repository.RoleRepository;
import mathes.nametala.cadernetaapi.services.RoleService;

@Service
public class RoleServiceImpl implements RoleService{

	@Autowired
	private RoleRepository roleRepository;
	
	@Override
	public List<RoleEntity> getRoles() {
		return roleRepository.findAll();
	}
	
}
