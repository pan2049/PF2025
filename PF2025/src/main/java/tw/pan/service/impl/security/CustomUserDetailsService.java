package tw.pan.service.impl.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import tw.pan.dao.AccessDao;
import tw.pan.entity.po.Access;

@Service
public class CustomUserDetailsService implements UserDetailsService{

	@Autowired
	private AccessDao accessDao;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		if(username == null) throw new UsernameNotFoundException("request username is null");
		Access access = accessDao.selectAccessInfo(username);
		if (access != null) {
			return User.withUsername(username)
				     .password(access.getPassword())
				     .roles("S")
				     .build();
		} else {
			throw new UsernameNotFoundException("No Such Account Found");
		}
	}

	
}
