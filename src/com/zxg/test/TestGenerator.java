package com.zxg.test;

import com.zxg.dao.EmployeeMapper;
import com.zxg.mybatis.Employee;
import com.zxg.mybatis.EmployeeExample;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.Before;
import org.junit.Test;
import org.mybatis.generator.api.MyBatisGenerator;
import org.mybatis.generator.config.Configuration;
import org.mybatis.generator.config.xml.ConfigurationParser;
import org.mybatis.generator.exception.InvalidConfigurationException;
import org.mybatis.generator.exception.XMLParserException;
import org.mybatis.generator.internal.DefaultShellCallback;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by zxg on 2017/6/9.
 */
public class TestGenerator {

    @Test
    public void testMbg() {
        List<String> warnings = new ArrayList<>();
        boolean overwrite = true;
        File configFile = new File("mbg.xml");
        ConfigurationParser cp = new ConfigurationParser(warnings);
        try {
            Configuration config = cp.parseConfiguration(configFile);
            DefaultShellCallback callback = new DefaultShellCallback(overwrite);
            MyBatisGenerator myBatisGenerator = new MyBatisGenerator(config, callback, warnings);
            myBatisGenerator.generate(null);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (XMLParserException e) {
            e.printStackTrace();
        } catch (InvalidConfigurationException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private SqlSessionFactory sqlSessionFactory = null;

    @Before
    public void init() {
        String resource = "mybatis-config.xml";
        InputStream inputStream = null;
        try {
            inputStream = Resources.getResourceAsStream(resource);
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
    }

    @Test
    public void testSimple() {
        SqlSession sqlSession = this.sqlSessionFactory.openSession(true);

        try {
            EmployeeMapper employeeMapper = sqlSession.getMapper(EmployeeMapper.class);
            int flag = employeeMapper.deleteByPrimaryKey(12);
            System.out.println("flag = " + flag);
        } finally {
            sqlSession.close();
        }
    }

    @Test
    public void testMybatis3() {
        SqlSession sqlSession = this.sqlSessionFactory.openSession(true);

        try {
            EmployeeMapper employeeMapper = sqlSession.getMapper(EmployeeMapper.class);
            Employee employee = employeeMapper.selectByPrimaryKey(1);
            System.out.println("employee = " + employee);
        } finally {
            sqlSession.close();
        }
    }

    @Test
    public void testMybatis3Criteria() {
        SqlSession sqlSession = this.sqlSessionFactory.openSession(true);

        try {
            EmployeeMapper employeeMapper = sqlSession.getMapper(EmployeeMapper.class);

            EmployeeExample employeeExample = new EmployeeExample();
            EmployeeExample.Criteria criteria = employeeExample.createCriteria();
            criteria.andLastNameLike("%xia%");
            criteria.andGenderEqualTo("1");

            EmployeeExample.Criteria criteria1 = employeeExample.createCriteria();
            criteria1.andEmailLike("%571%");
            employeeExample.or(criteria1);
            List<Employee> employees = employeeMapper.selectByExample(employeeExample);
            employees.stream().forEach(System.out::println);
        } finally {
            sqlSession.close();
        }
    }
}

