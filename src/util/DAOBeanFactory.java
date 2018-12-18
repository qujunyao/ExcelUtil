package util;

import java.io.Serializable;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * 取得DAO的BEAN. 初始化BeanFactory 通过配置文件中beanId获得factory中的bean
 */
public class DAOBeanFactory implements Serializable {
	private static Log log = LogFactory.getLog(DAOBeanFactory.class);

	private static final long serialVersionUID = 1L;

	/**
	 * spring context file path
	 */
	private final static String beanConfig = "date_new.xml";

	private static BeanFactory factory;

	private static Properties props = getProps();

	/**
	 * 初始化context对象，通过beanConfig指定的context file
	 */
	public static void init() {
		ApplicationContext aContext = new ClassPathXmlApplicationContext(beanConfig);
		log.debug("初始化context:" + beanConfig);
		factory = aContext;

	}

	/**
	 * 通过beanId得到factory中的bean实例
	 * 
	 * @param beanId
	 * @return Object
	 */
	public static Object getBean(String beanId) {
		Object obj = null;

		if (factory == null) {
			synchronized (DAOBeanFactory.class) {
				if (factory == null) {
					init();
				}
			}
		}
		if (factory != null)
			obj = factory.getBean(beanId);
		return obj;
	}

	/**
	 * 获得BeanFactory实例
	 * 
	 * @return the BeanFactory
	 */
	public static BeanFactory getBeanFactory() {
		if (factory == null) {
			synchronized (DAOBeanFactory.class) {
				if (factory == null) {
					init();
				}
			}
		}
		return factory;
	}

	/**
	 * 获得系统配置属性对象
	 * 
	 * @return 属性对象
	 */
	public static Properties getProps() {
		Properties conf = null;
		try {
			conf = (Properties) getBean("config");
		} catch (NoSuchBeanDefinitionException e) {
			log.error("没有找到config的名字！");
		}
		return conf;
	}

	/**
	 * 得到系统配置属性
	 * 
	 * @param name
	 * @return 属性字符值
	 */
	public static String getConfig(String name) {
		if (props != null)
			return (String) props.getProperty(name);
		else
			return null;
	}

}
