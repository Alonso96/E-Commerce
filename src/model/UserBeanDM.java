package model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.LinkedList;

public class UserBeanDM implements UserModel<UserBean> {
	
	private static final String  TABLE_NAME ="utenti";

	@Override 
	public Collection<UserBean> retrieveAllUsers() throws SQLException {
		Connection connection = null;
		PreparedStatement preparedStatement = null;

		Collection<UserBean> list = new LinkedList<UserBean>();

		String selectSQL = "SELECT * FROM " + TABLE_NAME ;

		try {
			connection = DriverManagerConnectionPool.getConnection(); // crea la connessione se non esiste
			preparedStatement = connection.prepareStatement(selectSQL);

			System.out.println("doRetrieveAll" + preparedStatement.toString());

			ResultSet rs = preparedStatement.executeQuery(); // la query viene eseguita

			while(rs.next()) {
				UserBean bean = new UserBean(); // salvo gli attributi letti dal result set , farlo per ogni tupla
				bean.setFirstName("Nome");
				bean.setLastName("Cognome");
				bean.setAddress("indirizzo");
				
				bean.setCity("Città");
				bean.seteMail("Email");
				bean.setPassword("Password");
				bean.setUserName("Nickname");
				

				list.add(bean); // salvo nella collezione
			}

		} finally {

			try {
				if(preparedStatement != null ) {
					preparedStatement.close(); // rilascio risorse
				}
			}
			finally {
				DriverManagerConnectionPool.releaseConnection(connection); // evita di far reinstanziare ogni volta una connection
				// la connection viene "conservata" nella collection Pool
			}
		}

		return list;
	}

	@Override
	public void saveUsers(UserBean user) throws SQLException {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		String uname ;
		uname= user.getUserName(); // procedura per vedere se esiste utente
		String checkSQL="Select Nickname from "+UserBeanDM.TABLE_NAME+" where Nickname=" + uname+ ";";
	
		System.out.println("Inserisco utente");
		String insertSQL = "INSERT INTO " + UserBeanDM.TABLE_NAME +
				"( Nome, Cognome, Nickname,Indirizzo,Admin, Email, Password, Cap,Città)" +
				"VALUES (?,?,?,?,?,?,?,?,? );";
		try {
			connection = DriverManagerConnectionPool.getConnection();
			preparedStatement = connection.prepareStatement(insertSQL);
			preparedStatement.setString(1, user.getFirstName());
			preparedStatement.setString(2, user.getLastName());
			preparedStatement.setString(3, user.getUserName());
			preparedStatement.setString(4,  user.getAddress());
			preparedStatement.setInt(5,0);
			preparedStatement.setString(7, user.getPassword());
			preparedStatement.setString(6, user.geteMail());
			preparedStatement.setString(8, user.getCap());
			preparedStatement.setString(9, user.getCity());
			// seguire la tabella nel db e fare il settaggio
			
			
			
			
			System.out.println("doSave: "+ preparedStatement.toString());
			preparedStatement.executeUpdate();

			connection.commit();
		} finally {
			try {
				if (preparedStatement != null)
					preparedStatement.close();
			} finally {
				DriverManagerConnectionPool.releaseConnection(connection);
			}

		}
	}
	public static boolean checkUser(String uname ) throws SQLException {
		boolean flag =false;
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		String checkSQL="Select Nickname from "+UserBeanDM.TABLE_NAME+" where Nickname= ?;" ;
		
		try {
			try {
				connection = DriverManagerConnectionPool.getConnection();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} // crea la connessione se non esiste
			preparedStatement = connection.prepareStatement(checkSQL);

			preparedStatement.setString(1,uname);
		

			System.out.println("validate..." + preparedStatement.toString());

			ResultSet rs = preparedStatement.executeQuery(); // la query viene eseguita
			
			flag=rs.next();
			
		} finally {

			try {
				if(preparedStatement != null ) {
					preparedStatement.close(); // rilascio risorse
				}
			}
			finally {
				DriverManagerConnectionPool.releaseConnection(connection); // evita di far reinstanziare ogni volta una connection
				// la connection viene "conservata" nella collection Pool
			}
		}

		return flag;
		
		
	}
	@Override
	public boolean deleteUsers(String userName) throws SQLException {
		Connection connection = null;
		PreparedStatement preparedStatement = null;

		int result = 0;

		String deleteSQL = "DELETE FROM " + UserBeanDM.TABLE_NAME + " WHERE Nickname = "+userName;

		try {
			connection = DriverManagerConnectionPool.getConnection();
			preparedStatement = connection.prepareStatement(deleteSQL);
			preparedStatement.setString(0, userName);

			System.out.println("doDelete: "+ preparedStatement.toString());
			result = preparedStatement.executeUpdate();

			connection.commit();
		} finally {
			try {
				if (preparedStatement != null)
					preparedStatement.close();
			} finally {
				DriverManagerConnectionPool.releaseConnection(connection);
			}
		}
		return (result != 0);
	}
	

	@Override
	public UserBean retrieveByKey(String userName) throws SQLException {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		UserBean bean = new UserBean();
		String selectSQL = "SELECT * FROM " + UserBeanDM.TABLE_NAME + " WHERE Nickname = " + userName +";";
				
		try {
			try {
				connection = DriverManagerConnectionPool.getConnection();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} // crea la connessione se non esiste
			preparedStatement = connection.prepareStatement(selectSQL);

			preparedStatement.setString(0, userName);

			System.out.println("doRetrieveByKey" + preparedStatement.toString());

			ResultSet rs = preparedStatement.executeQuery(); // la query viene eseguita

			while(rs.next()) {
				bean.setFirstName("Nome");
				bean.setLastName("Cognome");
				bean.setAddress("indirizzo");
				
				bean.setCity("Città");
				bean.seteMail("Email");
				bean.setPassword("Password");
				bean.setUserName("Nickname");	
			}
		} finally {

			try {
				if(preparedStatement != null ) {
					preparedStatement.close(); // rilascio risorse
				}
			}
			finally {
				DriverManagerConnectionPool.releaseConnection(connection); // evita di far reinstanziare ogni volta una connection
				// la connection viene "conservata" nella collection Pool
			}
		}

		return bean;
	}
	
	
	// VERIFICA SE utente e password coincidono
	
	public static boolean  validate(String userName, String password) throws SQLException {
			boolean status = false;
			Connection connection = null;
			PreparedStatement preparedStatement = null;
			UserBean bean = new UserBean();
			String selectSQL = "SELECT * FROM " + UserBeanDM.TABLE_NAME + " WHERE Nickname = ? and Password =? ;";
					
			try {
				try {
					connection = DriverManagerConnectionPool.getConnection();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} // crea la connessione se non esiste
				preparedStatement = connection.prepareStatement(selectSQL);

				preparedStatement.setString(1, userName);
				preparedStatement.setString(2, password) ;

				System.out.println("validate..." + preparedStatement.toString());

				ResultSet rs = preparedStatement.executeQuery(); // la query viene eseguita

				status=rs.next();
				
			} finally {

				try {
					if(preparedStatement != null ) {
						preparedStatement.close(); // rilascio risorse
					}
				}
				finally {
					DriverManagerConnectionPool.releaseConnection(connection); // evita di far reinstanziare ogni volta una connection
					// la connection viene "conservata" nella collection Pool
				}
			}

			return status;
		}
	
	public static int getFlag(String userName, String password) throws SQLException{
		int flag =-1;
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		UserBean bean = new UserBean();
		String selectSQL = "SELECT Admin FROM " + UserBeanDM.TABLE_NAME + " WHERE Nickname = ? and Password =? ;";
				
		try {
			try {
				connection = DriverManagerConnectionPool.getConnection();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} // crea la connessione se non esiste
			preparedStatement = connection.prepareStatement(selectSQL);

			preparedStatement.setString(1, userName);
			preparedStatement.setString(2, password) ;

			System.out.println("validate..." + preparedStatement.toString());

			ResultSet rs = preparedStatement.executeQuery(); // la query viene eseguita
			if(rs.next())
			flag=rs.getInt("Admin");
			
		} finally {

			try {
				if(preparedStatement != null ) {
					preparedStatement.close(); // rilascio risorse
				}
			}
			finally {
				DriverManagerConnectionPool.releaseConnection(connection); // evita di far reinstanziare ogni volta una connection
				// la connection viene "conservata" nella collection Pool
			}
		}

		return flag;
	}
	}

