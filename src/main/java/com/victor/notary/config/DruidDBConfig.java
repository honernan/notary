package com.victor.notary.config;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.support.http.StatViewServlet;
import com.alibaba.druid.support.http.WebStatFilter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;


/**
 * Druid数据源配置
 */
@Configuration
public class DruidDBConfig {

    private static String dbUrl;

    private static String username;

    private static String password;

    private static String driverClassName;

    private static int initialSize;

    private static int minIdle;

    private static int maxActive;

    private static int maxWait;

    /**
     * 注册DruidServlet
     *
     * return
     */
    @Bean
    public ServletRegistrationBean druidServletRegistrationBean() {
        ServletRegistrationBean servletRegistrationBean = new ServletRegistrationBean();
        servletRegistrationBean.setServlet(new StatViewServlet());
        servletRegistrationBean.addUrlMappings("/druid/*");
        //登录查看信息的账号密码.
        servletRegistrationBean.addInitParameter("loginUsername", "admin");
        servletRegistrationBean.addInitParameter("loginPassword", "123456");
        return servletRegistrationBean;
    }

    /**
     * 注册DruidFilter拦截
     *
     * return
     */
    @Bean
    public FilterRegistrationBean druidFilterRegistrationBean() {
        FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean();
        filterRegistrationBean.setFilter(new WebStatFilter());
        Map<String, String> initParams = new HashMap<>();
        //设置忽略请求
        initParams.put("exclusions", "*.js,*.gif,*.jpg,*.bmp,*.png,*.css,*.ico,/druid/*");
        filterRegistrationBean.setInitParameters(initParams);
        filterRegistrationBean.addUrlPatterns("/*");
        return filterRegistrationBean;
    }

    /**
     * 配置DataSource
     * return
     * throws SQLException
     */
    @Bean//(initMethod = "init",destroyMethod = "close")
    @Primary
    public DataSource dataSource() throws SQLException {
        DruidDataSource druidDataSource = new DruidDataSource();
        druidDataSource.setUsername(username);
        druidDataSource.setPassword(password);
        druidDataSource.setUrl(dbUrl);
        druidDataSource.setFilters("stat,config");
        druidDataSource.setInitialSize(initialSize);
        druidDataSource.setMinIdle(minIdle);
        druidDataSource.setMaxActive(maxActive);
        druidDataSource.setMaxWait(maxWait);
        druidDataSource.setUseGlobalDataSourceStat(true);
        druidDataSource.setDriverClassName(driverClassName);
        return druidDataSource;
    }

    @Value("${spring.datasource.druid.url}")
    public void setDbUrl(String dbUrl) {
        DruidDBConfig.dbUrl = dbUrl;
    }

    @Value("${spring.datasource.druid.username}")
    public void setUsername(String username) {
        DruidDBConfig.username = username;
    }

    @Value("${spring.datasource.druid.password}")
    public void setPassword(String password) {
        DruidDBConfig.password = password;
    }

    @Value("${spring.datasource.druid.driver-class-name}")
    public void setDriverClassName(String driverClassName) {
        DruidDBConfig.driverClassName = driverClassName;
    }

    @Value(value = "${spring.datasource.druid.initialSize}")
    public void setInitialSize(int initialSize) {
        DruidDBConfig.initialSize = initialSize;
    }

    @Value(value = "${spring.datasource.druid.minIdle}")
    public void setMinIdle(int minIdle) {
        DruidDBConfig.minIdle = minIdle;
    }

    @Value(value = "${spring.datasource.druid.maxActive}")
    public void setMaxActive(int maxActive) {
        DruidDBConfig.maxActive = maxActive;
    }

    @Value(value = "${spring.datasource.druid.maxWait}")
    public void setMaxWait(int maxWait) {
        DruidDBConfig.maxWait = maxWait;
    }

}