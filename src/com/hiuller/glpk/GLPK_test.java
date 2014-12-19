package com.hiuller.glpk;

import org.gnu.glpk.GLPK;
import org.gnu.glpk.GLPKConstants;
import org.gnu.glpk.SWIGTYPE_p_double;
import org.gnu.glpk.SWIGTYPE_p_int;
import org.gnu.glpk.glp_prob;

public class GLPK_test
{

	public static void main(String[] args)
	{
		System.out.printf("GLPK Solver Version = %s\n", GLPK.glp_version());
		
		// in this tutorial, we'll create a simple LP model
		
		// indices for rows and columns. Also values
		SWIGTYPE_p_int rows;
		SWIGTYPE_p_int cols;
		SWIGTYPE_p_double vals;
		// return flag
		int ret;
		// solution
		double x1, x2, x3, z;
		
		glp_prob problema = null;
		try{
						
			problema = GLPK.glp_create_prob();
			GLPK.glp_set_prob_name(problema, "Hiuller");
			
			// define the rows (linear constraints)
			GLPK.glp_add_rows(problema, 3);
			//
			GLPK.glp_set_row_name(problema, 1, "p");
			GLPK.glp_set_row_bnds(problema, 1, GLPKConstants.GLP_UP, 0.0, 100.0);
			//
			GLPK.glp_set_row_name(problema, 2, "q");
			GLPK.glp_set_row_bnds(problema, 2, GLPKConstants.GLP_UP, 0.0, 600.0);
			//
			GLPK.glp_set_row_name(problema, 3, "r");
			GLPK.glp_set_row_bnds(problema, 3, GLPKConstants.GLP_UP, 0.0, 300.0);
			
			// define columns (decision variables)
			GLPK.glp_add_cols(problema, 3);
			//
			GLPK.glp_set_col_name(problema, 1, "x1");
			GLPK.glp_set_col_bnds(problema, 1, GLPKConstants.GLP_LO, 0.0, 0.0);
			GLPK.glp_set_obj_coef(problema, 1, 10.0);
			//
			GLPK.glp_set_col_name(problema, 2, "x2");
			GLPK.glp_set_col_bnds(problema, 2, GLPKConstants.GLP_LO, 0.0, 0.0);
			GLPK.glp_set_obj_coef(problema, 2, 06.0);
			//
			GLPK.glp_set_col_name(problema, 3, "x3");
			GLPK.glp_set_col_bnds(problema, 3, GLPKConstants.GLP_LO, 0.0, 0.0);
			GLPK.glp_set_obj_coef(problema, 3, 04.0);
			
			// define parameters
			rows = GLPK.new_intArray(9);
			GLPK.intArray_setitem(rows, 1, 1);
			GLPK.intArray_setitem(rows, 2, 1);
			GLPK.intArray_setitem(rows, 3, 1);
			GLPK.intArray_setitem(rows, 4, 2);
			GLPK.intArray_setitem(rows, 5, 3);
			GLPK.intArray_setitem(rows, 6, 2);
			GLPK.intArray_setitem(rows, 7, 3);
			GLPK.intArray_setitem(rows, 8, 2);
			GLPK.intArray_setitem(rows, 9, 3);
			cols = GLPK.new_intArray(9);
			GLPK.intArray_setitem(cols, 1, 1);
			GLPK.intArray_setitem(cols, 2, 2);
			GLPK.intArray_setitem(cols, 3, 3);
			GLPK.intArray_setitem(cols, 4, 1);
			GLPK.intArray_setitem(cols, 5, 1);
			GLPK.intArray_setitem(cols, 6, 2);
			GLPK.intArray_setitem(cols, 7, 2);
			GLPK.intArray_setitem(cols, 8, 3);
			GLPK.intArray_setitem(cols, 9, 3);
			vals = GLPK.new_doubleArray(9);
			GLPK.doubleArray_setitem(vals, 1, 1.0);
			GLPK.doubleArray_setitem(vals, 2, 1.0);
			GLPK.doubleArray_setitem(vals, 3, 1.0);
			GLPK.doubleArray_setitem(vals, 4, 10.0);
			GLPK.doubleArray_setitem(vals, 5, 2.0);
			GLPK.doubleArray_setitem(vals, 6, 4.0);
			GLPK.doubleArray_setitem(vals, 7, 2.0);
			GLPK.doubleArray_setitem(vals, 8, 5.0);
			GLPK.doubleArray_setitem(vals, 9, 6.0);
			//
			GLPK.glp_load_matrix(problema, 9, rows, cols, vals);
			
			// define objective
			GLPK.glp_set_obj_dir(problema, GLPKConstants.GLP_MAX);
			
			// solve
			ret = GLPK.glp_simplex(problema, null);
			
			if(ret == 0)
				System.out.println("Problem solved");
			else
			{
				System.out.println("The problem couldn't be solved.");
				System.exit(-1);
			}
			
			// writing the solution
			x1 = GLPK.glp_get_col_prim(problema, 1);
			x2 = GLPK.glp_get_col_prim(problema, 2);
			x3 = GLPK.glp_get_col_prim(problema, 3);
			z  = GLPK.glp_get_obj_val(problema);
			//
			System.out.printf("Solution details: z=%g (x1=%g; x2=%g, x3=%g).\n", z, x1, x2, x3);			
			
			// release memory
			GLPK.glp_delete_prob(problema);
				
			
		} catch(Exception e)
		{
			e.printStackTrace();
		}
	}

}
