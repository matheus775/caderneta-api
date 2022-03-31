package mathes.nametala.cadernetaapi.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import mathes.nametala.cadernetaapi.exceptionhandler.myExceptions.IdNotFoundException;
import mathes.nametala.cadernetaapi.model.entitys.AccountEntity;
import mathes.nametala.cadernetaapi.model.entitys.RoleEntity;
import mathes.nametala.cadernetaapi.repository.AccountRepositoy;
import mathes.nametala.cadernetaapi.repository.RoleRepository;
import mathes.nametala.cadernetaapi.repository.filter.AccountFilter;
import mathes.nametala.cadernetaapi.services.AccountService;

@Service
public class AccountServiceImpl implements AccountService {

	@Autowired
	private AccountRepositoy accountRepositoy;

	@Autowired
	private RoleRepository roleRepository;

	@Override
	public Page<AccountEntity> getAccounts(AccountFilter accountFilter, Pageable pageable) {
		if (accountFilter.getRoles() != null) {
			accountFilter.getRoles().stream().forEach(roleId -> {
				if (roleRepository.findById(roleId).isEmpty())
					throw new IdNotFoundException(roleId, RoleEntity.class);
			});
		}
		return accountRepositoy.filter(accountFilter, pageable);
	}

	@Override
	public AccountEntity getAccount(Long id) {
		return accountRepositoy.findById(id).get();
	}

	@Override
	public AccountEntity newAccount(AccountEntity account) {
		this.verifyRoleId(account);
		return accountRepositoy.save(account);
	}

	@Override
	public void delAccount(Long id) {
		accountRepositoy.deleteById(id);
	}

	@Override
	public AccountEntity updtAccount(AccountEntity account, Long id) {
		AccountEntity accountdB = accountRepositoy.findById(id).get();

		this.verifyRoleId(accountdB);

		accountdB.setUsername(account.getUsername());
		accountdB.setEmail(account.getEmail());
		accountdB.setPassword(account.getPassword());
		accountdB.setRoles(account.getRoles());
		return accountRepositoy.save(accountdB);
	}

	public void verifyRoleId(AccountEntity account) {
		account.getRoles().stream().mapToLong(RoleEntity::getId).forEach(roleId -> {
			if (roleRepository.findById(roleId).isEmpty())
				throw new IdNotFoundException(roleId, RoleEntity.class);
		});
	}

}
