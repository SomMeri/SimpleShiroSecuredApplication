package org.meri.simpleshirosecuredapplication.realm;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import org.apache.shiro.authc.AccountException;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.realm.jdbc.JdbcRealm;
import org.apache.shiro.util.JdbcUtils;
import org.apache.shiro.util.SimpleByteSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JNDIAndSaltAwareJdbcRealm extends JdbcRealm {

    private static final Logger log = LoggerFactory.getLogger(JNDIAndSaltAwareJdbcRealm.class);
    
    protected String jndiDataSourceName;

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        UsernamePasswordToken upToken = (UsernamePasswordToken) token;
        String username = upToken.getUsername();

        // Null username is invalid
        if (username == null) {
            throw new AccountException("Null usernames are not allowed by this realm.");
        }

        Connection conn = null;
        AuthenticationInfo info = null;
        try {
            conn = dataSource.getConnection();

            PasswdSalt passwdSalt = getPasswordForUser(conn, username);

            if (passwdSalt == null) {
                throw new UnknownAccountException("No account found for user [" + username + "]");
            }

            SimpleAuthenticationInfo saInfo = new SimpleAuthenticationInfo(username, passwdSalt.password, getName());
            saInfo.setCredentialsSalt(new SimpleByteSource(passwdSalt.salt));

            info = saInfo;

        } catch (SQLException e) {
            final String message = "There was a SQL error while authenticating user [" + username + "]";
            if (log.isErrorEnabled()) {
                log.error(message, e);
            }

            // Rethrow any SQL errors as an authentication exception
            throw new AuthenticationException(message, e);
        } finally {
            JdbcUtils.closeConnection(conn);
        }

        return info;
    }

    private PasswdSalt getPasswordForUser(Connection conn, String username) throws SQLException {

        PreparedStatement ps = null;
        ResultSet rs = null;
        String password = null;
        String salt = null;
        try {
            ps = conn.prepareStatement(authenticationQuery);
            ps.setString(1, username);

            // Execute query
            rs = ps.executeQuery();

            // Loop over results - although we are only expecting one result, since usernames should be unique
            boolean foundResult = false;
            while (rs.next()) {

                // Check to ensure only one row is processed
                if (foundResult) {
                    throw new AuthenticationException("More than one user row found for user [" + username + "]. Usernames must be unique.");
                }

                password = rs.getString(1);
                if (rs.getMetaData().getColumnCount()>1)
                	salt = rs.getString(2);

                foundResult = true;
            }
        } finally {
            JdbcUtils.closeResultSet(rs);
            JdbcUtils.closeStatement(ps);
        }

        return new PasswdSalt(password, salt);
    }

		public String getJndiDataSourceName() {
    	return jndiDataSourceName;
    }

		public void setJndiDataSourceName(String jndiDataSourceName) {
    	this.jndiDataSourceName = jndiDataSourceName;
    	this.dataSource =	getDataSourceFromJNDI(jndiDataSourceName);
    }

		private DataSource getDataSourceFromJNDI(String jndiDataSourceName) {
      try {
	      InitialContext ic = new InitialContext();
	      return (DataSource) ic.lookup(jndiDataSourceName);
      } catch (NamingException e) {
        log.error("JNDI error while retrieving " + jndiDataSourceName, e);
      	throw new AuthorizationException(e);
      }   
    }

   
}

class PasswdSalt {

	public String password;
	public String salt;
	
	public PasswdSalt(String password, String salt) {
	  super();
	  this.password = password;
	  this.salt = salt;
  }
	
}