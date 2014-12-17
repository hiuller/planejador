package com.hiuller.glpk;

import org.gnu.glpk.GLPK;

public class GLPK_test
{

	public static void main(String[] args)
	{
		System.out.printf("GLPK Solver test...Version = %s\n", GLPK.glp_version());
	}

}
