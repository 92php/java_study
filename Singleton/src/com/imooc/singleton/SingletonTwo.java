package com.imooc.singleton;

//����ʽ������ʵ�����󴴽�ʱ����ֱ�ӳ�ʼ����ֱ����һ�ε���get����ʱ������ɳ�ʼ������
//ʱ�任�ռ�
public class SingletonTwo {
	//1������˽�й��췽��
	private SingletonTwo(){
		
	}
	
	//2��������̬�ĸ���ʵ������
	private static SingletonTwo instance=null;
	
	//3���������ŵľ�̬�����ṩʵ������
	public static SingletonTwo getInstance(){
		if(instance==null)
			instance=new SingletonTwo();
		
		return instance;
	}
}