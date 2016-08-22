package com.tricast.web.manager;

import java.sql.SQLException;
import java.util.List;

import javax.inject.Inject;

import com.tricast.beans.Account;
import com.tricast.web.annotations.JdbcTransaction;
import com.tricast.web.dao.AccountDao;
import com.tricast.web.dao.HolidayDao;
import com.tricast.web.dao.Workspace;

public class AccountManagerImpl implements AccountManager {

	private final AccountDao accountDao;
	private final HolidayDao leaveDao;

	@Inject
	public AccountManagerImpl(AccountDao accountDao, HolidayDao leaveDao) {
		this.accountDao = accountDao;
		this.leaveDao = leaveDao;
	}

	@Override
	@JdbcTransaction
	public List<Account> getAll(Workspace workspace) throws SQLException {
		return accountDao.getAll(workspace);
	}

	@Override
	@JdbcTransaction
	public Account getById(Workspace workspace, long id) throws SQLException {
		Account account = accountDao.getById(workspace, id);
		account.setHolidays(leaveDao.getAllForAccount(workspace, account.getId()));
		return account;
	}

	@Override
	@JdbcTransaction
	public Account create(Workspace workspace, Account newAccount) throws SQLException {
		Long id = accountDao.create(workspace, newAccount);
		if (id != null) {
			return accountDao.getById(workspace, id);
		} else {
			return null;
		}
	}

	@Override
	@JdbcTransaction
	public Account update(Workspace workspace, Account updateAccount) throws SQLException {
		Long id = accountDao.update(workspace, updateAccount);
		if (id != null) {
			return accountDao.getById(workspace, id);
		} else {
			return null;
		}
	}

	@Override
	@JdbcTransaction
	public boolean deleteById(Workspace workspace, long Id) throws SQLException {
		return accountDao.deleteById(workspace, Id);
	}

	@Override
	@JdbcTransaction
	public Account login(Workspace workspace, String username, String password) throws SQLException {
		Account account = accountDao.login(workspace, username, password);
		if (account == null) {
			throw new SQLException("No account exists with the specified username:" + username);
		}
		account.setHolidays(leaveDao.getAllForAccount(workspace, account.getId()));
		return account;
	}

}
