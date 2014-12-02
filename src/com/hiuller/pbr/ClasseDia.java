package com.hiuller.pbr;

import hiuller.objectmodel.Plano;

import java.util.Iterator;

public class ClasseDia
{
	
	public static final int
		normal1		= 0,
		normal2		= 1,
		campanha1	= 2,
		campanha2	= 3,
		ld1			= 4,
		ld2         = 5,
		parada1		= 6,
		parada2		= 7,
		paradaRH	= 8,
		inspecao1   = 9,
		inspecao2   =10,
		paradaMLC1  =11,
		paradaMLC2  =12;
	
	public ClasseDia(Plano plano)
	{
		Iterator<String> a = plano.getIteratorParadas();
		int n = plano.getNumDias();
	}
}
