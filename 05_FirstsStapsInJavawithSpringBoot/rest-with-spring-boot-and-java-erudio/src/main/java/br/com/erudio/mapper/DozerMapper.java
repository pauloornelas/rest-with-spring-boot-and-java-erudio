package br.com.erudio.mapper;

import java.util.ArrayList;
import java.util.List;

import com.github.dozermapper.core.DozerBeanMapperBuilder;
import com.github.dozermapper.core.Mapper;

public class DozerMapper {

	private static Mapper mapper = DozerBeanMapperBuilder.buildDefault();

	public static <O, D> D parseObject(O origen, Class<D> destination) {
		return mapper.map(origen, destination);
	}

	public static <O, D> List<D> parseListObjects(List<O> origen, Class<D> destination) {
		List<D> destinatinationObjects = new ArrayList<D>();
		for (O o : origen) {
			destinatinationObjects.add(mapper.map(o, destination));
		}
		return destinatinationObjects;
	}

}
