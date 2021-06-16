package ch03;
import java.sql.ResultSet;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
public class Ex01 {
	public static void main(String[] args) {
		AbstractApplicationContext ac=
			new ClassPathXmlApplicationContext("db.xml");
		JdbcTemplate jt = ac.getBean(JdbcTemplate.class);
		NamedParameterJdbcTemplate npjp =	
				ac.getBean(NamedParameterJdbcTemplate.class);
		// int count = jt.queryForInt("select count(*) from pet");
		int count=jt.queryForObject("select count(*) from pet",Integer.class); 
		System.out.println("애완동물 갯수 : " + count);
		String ownerName = "kk";
		count = jt.queryForObject(
			"select count(*) from pet where ownerName=?"
				,Integer.class,ownerName);
		System.out.println("kk가 가진 애완동물 : "+count);
		int id = 1;
		String petName = jt.queryForObject(
			"select petName from pet where petId=?",
			String.class,id);
		System.out.println("애완동물 이름 : "+petName);
		Date dt = jt.queryForObject(
			"select birthDate from pet where petId=?",
			Date.class,id);
		System.out.println("애완동물 생일 : "+dt);
		//   컬럼명     값(문자, 숫자, 날자)
		Map<String, Object> map = jt.queryForMap(
			"select * from pet where petId=?",id);
		System.out.println("map 애완동물 이름 : "+map.get("petName"));
		System.out.println("map 애완동물 소유자 "+map.get("ownerName"));
		System.out.println("==================");
		List<Map<String, Object>> petList = jt.queryForList(
			"select * from pet where ownerName=?",ownerName);
		for (Map<String, Object> m1 : petList) {
			System.out.println("애완동물 이름 : "+m1.get("petName"));
			System.out.println("애완동물 소유자 "+m1.get("ownerName"));
		}
		System.out.println("==================");
		List<Integer> listPrice = jt.queryForList(
			"select price from pet where ownerName=?",
			Integer.class,ownerName);
		for (int price : listPrice) {
			System.out.println("애완동물 가격 : "+price);
		}
		System.out.println("==================");
		Pet pet = jt.queryForObject(
			"select * from pet where petId=?", 
			new RowMapper<Pet>() {
				public Pet mapRow(ResultSet rs, int arg1) throws SQLException {
					Pet p = new Pet();
					p.setPetId(rs.getInt("petId"));
					p.setPetName(rs.getString("petName"));
					p.setOwnerName(rs.getString("ownerName"));
					p.setPrice(rs.getInt("price"));
					return p;
				}
			},
		id);
		System.out.println("애완동물 주인 : "+pet.getOwnerName());
		System.out.println("애완동물 이름 : "+pet.getPetName());
		System.out.println("------------------");
		
		class MyRowMapper implements RowMapper<Pet> {
			public Pet mapRow(ResultSet rs, int arg1) throws SQLException {
				Pet p = new Pet();
				p.setPetId(rs.getInt("petId"));
				p.setPetName(rs.getString("petName"));
				p.setOwnerName(rs.getString("ownerName"));
				p.setPrice(rs.getInt("price"));
				return p;
			}			
		}
		pet = jt.queryForObject(
			"select * from pet where petId=?",
			new MyRowMapper(),id);
		System.out.println("애완동물 주인 : "+pet.getOwnerName());
		System.out.println("애완동물 이름 : "+pet.getPetName());
		System.out.println("------------------");
		
		List<Pet> list = jt.query(
			"select * from pet where ownerName=?",
			new MyRowMapper(),ownerName);
		for (Pet p : list) {
			System.out.println("애완동물 주인 : "+p.getOwnerName());
			System.out.println("애완동물 이름 : "+p.getPetName());
		}
		System.out.println("------------------");
		pet = jt.queryForObject(
				"select * from pet where petId=?", 
				new BeanPropertyRowMapper<Pet>(Pet.class),id);
		System.out.println("애완동물 주인 : "+pet.getOwnerName());
		System.out.println("애완동물 이름 : "+pet.getPetName());
		System.out.println("------------------");
		Owner owner = jt.query(
	        " SELECT * FROM OWNER O, PET P where O.OWNERNAME=P.OWNERNAME and O.OWNERNAME=?"
	        , new ResultSetExtractor<Owner>() {
	        public Owner extractData(ResultSet rs) throws SQLException, DataAccessException {
	        	 if (!rs.next()) {  return null;    }
	        	Owner owner = new Owner();
                   owner.setOwnerName(rs.getString("OWNERNAME"));
                   do {
                       Pet pet = new Pet();
                       pet.setPetId(rs.getInt("PETID"));
                       pet.setPetName(rs.getString("PETNAME"));
                       pet.setOwnerName(rs.getString("OWNERNAME"));
                       pet.setPrice(rs.getInt("PRICE"));
                       pet.setBirthDate(rs.getDate("BIRTHDATE"));
                       owner.getPetList().add(pet);
                   } while(rs.next());
                   return owner;
               }}
           , ownerName);
		System.out.println("주인 이름 : "+owner.getOwnerName());
		for(int i= 0; i < owner.getPetList().size();i++) {
			System.out.println("애완이름 : "+
					owner.getPetList().get(i).getPetName());
			System.out.println("애완가격 : "+
					owner.getPetList().get(i).getPrice());
		}
		List<Owner> ownerList = jt.query(
                " SELECT * FROM OWNER O, PET P where O.OWNERNAME"
                + "=P.OWNERNAME ORDER BY p.OWNERNAME"
                , new ResultSetExtractor<List<Owner>>() {
                    public List<Owner> extractData(ResultSet rs) throws SQLException, DataAccessException {
                        List<Owner> result = new ArrayList<Owner>();
                        Owner owner = null;
                        String currentPk = "";
                        while (rs.next()) {
                            String ownerName = rs.getString("OWNERNAME");
                            if (!ownerName.equals(currentPk)) {
                                owner = new Owner();
                                owner.setOwnerName(rs.getString("OWNERNAME"));
                                currentPk = ownerName;
                                result.add(owner);
                            }
                            Pet pet = new Pet();
                            pet.setPetId(rs.getInt("PETID"));
                            pet.setPetName(rs.getString("PETNAME"));
                            pet.setOwnerName(rs.getString("OWNERNAME"));
                            pet.setPrice(rs.getInt("PRICE"));
                            pet.setBirthDate(rs.getDate("BIRTHDATE"));
                            owner.getPetList().add(pet);
                        }
                        return result;
                    }}
                );
        for(Owner ow :ownerList ) {
        	for (Pet p : ow.getPetList()) {
        		System.out.println(p.getOwnerName());
                System.out.println(p.getPetName());
                System.out.println("***************");
        	}
        }    
        
        pet = new Pet();
        pet.setPetId(04);
        pet.setPetName("망아지");
        pet.setOwnerName("kk");
        pet.setPrice(10000);
        pet.setBirthDate(new Date()); 
        jt.update(
                "INSERT INTO PET (PETID,PETNAME,OWNERNAME,"
                + "PRICE,BIRTHDATE) VALUES (?, ?, ?, ?, ?)"
                , pet.getPetId(),pet.getPetName(),pet.getOwnerName(),
                pet.getPrice(), pet.getBirthDate());
        
        jt.update(
                "UPDATE PET SET PETNAME=?, OWNERNAME=?, PRICE=?,"
                + " BIRTHDATE=? WHERE PETID=?"
                , pet.getPetName(), pet.getOwnerName(),
                pet.getPrice(), pet.getBirthDate(), pet.getPetId());
        
        jt.update("DELETE FROM PET WHERE PETID=?", 
        		pet.getPetId());

        npjp.update(
                " INSERT INTO PET (PETID, PETNAME, OWNERNAME, PRICE, BIRTHDATE)" +
                    " VALUES (:PETID, :PETNAME, :OWNERNAME, :PRICE, :BIRTHDATE)"
                , new MapSqlParameterSource()
                .addValue("PETID", pet.getPetId())
                .addValue("PETNAME", pet.getPetName())
                .addValue("OWNERNAME", pet.getOwnerName())
                .addValue("PRICE", pet.getPrice())
                .addValue("BIRTHDATE", pet.getBirthDate())
            );
        
        jt.update("DELETE FROM PET WHERE PETID=?", pet.getPetId());
        
        MapSqlParameterSource map2 = new MapSqlParameterSource();
        map2.addValue("PETID", pet.getPetId());
        map2.addValue("PETNAME", pet.getPetName());
        map2.addValue("OWNERNAME", pet.getOwnerName());
        map2.addValue("PRICE", pet.getPrice());
        map2.addValue("BIRTHDATE", pet.getBirthDate());
        npjp.update(
            " INSERT INTO PET (PETID, PETNAME, OWNERNAME, PRICE, BIRTHDATE)" +
                " VALUES (:PETID, :PETNAME, :OWNERNAME, :PRICE, :BIRTHDATE)"
            ,map2
        );
        
        jt.update("DELETE FROM PET WHERE PETID=?", pet.getPetId());

         BeanPropertySqlParameterSource beanProps 
         	= new BeanPropertySqlParameterSource(pet);
         npjp.update(
            " INSERT INTO PET (PETID, PETNAME, OWNERNAME, PRICE, BIRTHDATE)" +
                " VALUES (:petId, :petName, :ownerName, :price, :birthDate)"
            ,beanProps
        );
        
        jt.update("DELETE FROM PET WHERE PETID=?", pet.getPetId());
        ac.close();
    }    
  }