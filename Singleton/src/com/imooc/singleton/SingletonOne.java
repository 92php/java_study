package com.imooc.singleton;

//����ʽ����������ʵ����ʱ��ֱ�ӳ�ʼ��  �ռ任ʱ��
public class SingletonOne {
	//1����������˽�й���
	private SingletonOne(){
		
	}
	
	//2�����������͵�˽�о�̬ʵ��
	private static SingletonOne instance=new SingletonOne();
	
	//3���������о�̬�������ؾ�̬ʵ������
	public static SingletonOne getInstance(){
		return instance;
	}
}