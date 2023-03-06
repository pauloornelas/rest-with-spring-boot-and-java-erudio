package br.com.erudio.services;

import br.com.erudio.mathUtil.MathUtil;

public class MathServiceImpl implements MathService{
	
	MathUtil mathUtil = new MathUtil();

	public Double sum(Double numberOne, Double numberTwo) {
		return numberOne + numberTwo;
	}

	public Double subtraction(Double numberOne, Double numberTwo) {
		return numberOne - numberTwo;
	}

	public Double multiplication(Double numberOne, Double numberTwo) {
		return numberOne * numberTwo;
	}

	public Double division(Double numberOne, Double numberTwo) {
		return numberOne / numberTwo;
	}

	public Double mean(Double numberOne, Double numberTwo) {
		return (numberOne + numberTwo)/2;
	}

	public Double squareRoot(Double numberOne) {
		return Math.sqrt(numberOne);
	}

}
