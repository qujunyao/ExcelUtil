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
 * ȡ��DAO��BEAN. ��ʼ��BeanFactory ͨ�������ļ���beanId���factory�е�bean
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
	 * ��ʼ��context����ͨ��beanConfigָ����context file
	 */
	public static void init() {
		ApplicationContext aContext = new ClassPathXmlApplicationContext(beanConfig);
		log.debug("��ʼ��context:" + beanConfig);
		factory = aContext;

	}

	/**
	 * ͨ��beanId�õ�factory�е�beanʵ��
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
	 * ���BeanFactoryʵ��
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
	 * ���ϵͳ�������Զ���
	 * 
	 * @return ���Զ���
	 */
	public static Properties getProps() {
		Properties conf = null;
		try {
			conf = (Properties) getBean("config");
		} catch (NoSuchBeanDefinitionException e) {
			log.error("û���ҵ�config�����֣�");
		}
		return conf;
	}

	/**
	 * �õ�ϵͳ��������
	 * 
	 * @param name
	 * @return �����ַ�ֵ
	 */
	public static String getConfig(String name) {
		if (props != null)
			return (String) props.getProperty(name);
		else
			return null;
	}

}
