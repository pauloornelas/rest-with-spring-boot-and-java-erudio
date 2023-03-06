package br.com.erudio.controllers;

import java.util.concurrent.atomic.AtomicLong;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import br.com.erudio.exceptions.UnsupportedMathOperationException;
import br.com.erudio.mathUtil.MathUtil;
import br.com.erudio.services.MathServiceImpl;

@RestController
public class MathController {

	private MathServiceImpl mathServiceImpl = new MathServiceImpl();

	private static final AtomicLong counter = new AtomicLong();

	@RequestMapping(value = "/sum/{numberOne}/{numberTwo}", method = RequestMethod.GET)
	public Double sum(@PathVariable(value = "numberOne") String numberOne,
			@PathVariable(value = "numberTwo") String numberTwo) throws Exception {

		if (!MathUtil.isNumeric(numberOne) || !MathUtil.isNumeric(numberTwo)) {
			throw new UnsupportedMathOperationException("Please set a numeric value!");
		}

		return mathServiceImpl.sum(MathUtil.conertDouble(numberOne), MathUtil.conertDouble(numberTwo));
	}

	@RequestMapping(value = "/subtraction/{numberOne}/{numberTwo}", method = RequestMethod.GET)
	public Double subtraction(@PathVariable(value = "numberOne") String numberOne,
			@PathVariable(value = "numberTwo") String numberTwo) {

		if (!MathUtil.isNumeric(numberOne) || !MathUtil.isNumeric(numberTwo)) {
			throw new UnsupportedMathOperationException("Please set a numeric value!");
		}

		return mathServiceImpl.subtraction(MathUtil.conertDouble(numberOne), MathUtil.conertDouble(numberTwo));
	}

	@RequestMapping(value = "/multiplication/{numberOne}/{numberTwo}", method = RequestMethod.GET)
	public Double multiplication(@PathVariable(value = "numberOne") String numberOne,
			@PathVariable(value = "numberTwo") String numberTwo) {

		if (!MathUtil.isNumeric(numberOne) || !MathUtil.isNumeric(numberTwo)) {
			throw new UnsupportedMathOperationException("Please set a numeric value!");
		}

		return mathServiceImpl.multiplication(MathUtil.conertDouble(numberOne), MathUtil.conertDouble(numberTwo));
	}

	@RequestMapping(value = "/division/{numberOne}/{numberTwo}", method = RequestMethod.GET)
	public Double division(@PathVariable(value = "numberOne") String numberOne,
			@PathVariable(value = "numberTwo") String numberTwo) {

		if (!MathUtil.isNumeric(numberOne) || !MathUtil.isNumeric(numberTwo)) {
			throw new UnsupportedMathOperationException("Please set a numeric value!");
		}

		return mathServiceImpl.division(MathUtil.conertDouble(numberOne), MathUtil.conertDouble(numberTwo));
	}

	@RequestMapping(value = "/mean/{numberOne}/{numberTwo}", method = RequestMethod.GET)
	public Double mean(@PathVariable(value = "numberOne") String numberOne,
			@PathVariable(value = "numberTwo") String numberTwo) {

		if (!MathUtil.isNumeric(numberOne) || !MathUtil.isNumeric(numberTwo)) {
			throw new UnsupportedMathOperationException("Please set a numeric value!");
		}

		return mathServiceImpl.mean(MathUtil.conertDouble(numberOne), MathUtil.conertDouble(numberTwo));
	}

	@RequestMapping(value = "/squareRoot/{numberOne}", method = RequestMethod.GET)
	public Double squareRoot(@PathVariable(value = "numberOne") String numberOne) {

		if (!MathUtil.isNumeric(numberOne)) {
			throw new UnsupportedMathOperationException("Please set a numeric value!");
		}

		return mathServiceImpl.squareRoot(MathUtil.conertDouble(numberOne));
	}
}
