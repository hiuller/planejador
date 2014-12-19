package hiuller.objectmodel;

import gth.ColumnGroup;
import gth.GroupableTableColumnModel;
import gth.GroupableTableHeader;
import hiuller.gui.Constants;
import hiuller.gui.dialogs.EditorParametros;
import hiuller.gui.dialogs.InserirDemanda;
import hiuller.gui.utils.SolutionProvider;
import hiuller.gui.utils.Utils;
import hiuller.objectmodel.Equipamentos.Equip;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;

import javax.swing.BorderFactory;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JViewport;
import javax.swing.border.EtchedBorder;
import javax.swing.event.TableModelListener;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableModel;

import com.hiuller.pbr.ClasseDia;

public class Plano extends JPanel
{
	public static final String 
		FIELD_LIST = "dia,corr_ac1,carga_ac1,corr_ac2,carga_ac2,corr_transf,producao_aco,saldo,psuc_ac1,psuc_ac2,psuc_tot,prod_af1,prod_af2,prod_af3,prod_af,restr_hic,restr_dr,restr_rh,restr_fp2,aco_rh_tot,aco_fp2_tot,restr_fp1,aco_fp1_tot";
	
	public static final int
		FIELD_LIST_SIZE = 23;
		
	private static final String[] labels = new String[]
	{
		"AF", "Prim", "Sec", "Ling", //00-03 
		"Corr.1", "Peso 1 (t)", //04-05 
		"Corr.2", "Peso 2 (t)", //06-07 
		"Transf (t)", "Total", "Saldo", "Suc. Ac1", "Suc. Ac2", "Suc. Tot", //08-13*no group 
		"AF1", "AF2", "AF3", "Total AF", //14-17
		"HIC", "DR", "RH", "FP", "RH Tot", "FP Tot", //18-23
		"Corrida", "Peso" //24-25
	};
	
	private static final String UNIT_TON = "t";
	
	private static final long serialVersionUID = 1L;
	private Date periodo;
	private int numDias;
	// layout components	
	private JScrollPane
				   scroll;
	
	private JTable planilha;
	//
	private boolean hasData = false;
	
	private SolutionProvider provider;
	
	private double[] prod_diaria_af1,	// daily production for bf 1
					 prod_diaria_af2,	// daily production for bf 2
					 prod_diaria_af3;   // daily production for bf 3
	
	private double   prod_diaria_af1_total,
					 prod_diaria_af2_total,
					 prod_diaria_af3_total;
	
	private double[] saldo_diario;		// pig iron balance
// Aciaria
	private int[]    corridas_ac1,
					 corridas_ac2,
					 restr_hic,
					 restr_dr,
					 restr_rh,
					 restr_fp1,
				 	 restr_fp2;

	private double   					 
					 carga_med_ac1_total,
					 carga_med_ac2_total,
					 producao_aco_total,
					 producao_aco_ac1_total,
					 producao_aco_ac2_total,
					 suc_ac1_total,
					 suc_ac2_total,
					 suc_total;
	
	private double[] carga_med_ac1,
					 perc_sucata_ac1,
					 perc_sucata_ac2,
					 perc_sucata_total,
					 consumo_gusa_ac1,	
		             carga_med_ac2,					 
	 				 consumo_gusa_ac2,	
					 producao_aco,
					 producao_aco_ac1,
					 producao_aco_ac2;
	
	private int      corridas_ac1_total,
					 corridas_ac2_total,
					 restr_hic_total,
					 restr_dr_total,
					 restr_rh_total,
					 restr_fp1_total,
					 restr_fp2_total;
	 
	private double fitnessValue;

	//
	private InserirDemanda inserirDemanda;
	//
	private ArrayList<Parada> paradas;
	//
	private ArrayList<AlteracaoManual> alteracoes;
	
	public Plano(Date periodo)
	{
		super();
		
		this.inserirDemanda = new InserirDemanda();
		
		this.periodo = periodo;		
		this.numDias = computeNumDias();
		
		this.prod_diaria_af1 = new double[numDias];
		this.prod_diaria_af2 = new double[numDias];
		this.prod_diaria_af3 = new double[numDias];
		this.saldo_diario    = new double[numDias];

		this.corridas_ac1     = new int   [numDias];
		this.corridas_ac2     = new int   [numDias];		
		
		this.carga_med_ac1    = new double[numDias];
		this.perc_sucata_ac1  = new double[numDias];
		this.perc_sucata_total= new double[numDias];
		this.consumo_gusa_ac1 = new double[numDias];
		this.carga_med_ac2    = new double[numDias];
		this.perc_sucata_ac2  = new double[numDias];
		this.consumo_gusa_ac2 = new double[numDias];
		this.producao_aco     = new double[numDias];
		this.producao_aco_ac1 = new double[numDias];
		this.producao_aco_ac2 = new double[numDias];
		this.restr_hic        = new int   [numDias];
		this.restr_dr         = new int   [numDias];
		this.restr_rh         = new int   [numDias];
		this.restr_fp1        = new int   [numDias];
		this.restr_fp2        = new int   [numDias];		

// end of aciaria 1 e 2		
		this.paradas = new ArrayList<Parada>();
		this.alteracoes = new ArrayList<AlteracaoManual>();
		
		this.setBackground(Color.white);
				
		TableModel modelo = assembleModel();
		
	// grouping of headers
		planilha =  new JTable();
        planilha.setColumnModel(new GroupableTableColumnModel());
        planilha.setTableHeader(new GroupableTableHeader((GroupableTableColumnModel)planilha.getColumnModel()));
        planilha.setModel(modelo);
        
        Utils.setupTable(planilha);
        
     // take control over the appearance of the 'planilha' table
        // I have a list of paradas and I need to draw them
        setRenderers();
 // end of formatting                        
		setupHeaders();
        
        JTableHeader header = planilha.getTableHeader();
        header.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
        
       this.setLayout(new BorderLayout());
       scroll = new JScrollPane(planilha);
       scroll.setRowHeaderView(new ListaDias(this.numDias, 0));
       
       //http://www.java2s.com/Tutorial/Java/0240__Swing/SpecifyingFixedJTableColumns.htm
       ListaDias dias = new ListaDias(this.numDias, computeShift());
       JViewport viewport = new JViewport();
       viewport.setView(dias);
       viewport.setPreferredSize(dias.getPreferredSize());
       viewport.setMaximumSize(dias.getPreferredSize());
       scroll.setCorner(JScrollPane.UPPER_LEFT_CORNER, dias.getTableHeader());
       scroll.setRowHeaderView(viewport);       
       
       this.add(scroll, BorderLayout.CENTER);
// display the tooltip according with the ParadaLabel
       planilha.addMouseMotionListener(new MouseAdapter(){

		@Override
		public void mouseMoved(MouseEvent e)
		{
			String toolTip = null;
/*// force the tooltip to trigger
			Action toolTipAction = planilha.getActionMap().get("postTip");
			if(toolTipAction != null)
			{
				ActionEvent postTip = new ActionEvent((Component) planilha, ActionEvent.ACTION_FIRST, "postTip");
				toolTipAction.actionPerformed( postTip );
			}
			
*/
			int row = planilha.rowAtPoint(e.getPoint());			
			int col = planilha.columnAtPoint(e.getPoint()); // 0 <= col <= 3 are ParadaLabel
			
			if( col < 4 )
			{
				// col <=> group
				// dia <=> row+1
				int paradaCount = 0;
				toolTip = "<html>";
				for(Parada p:paradas)
				{
					if(p.getGroup() == col)
						if(p.occursInDay(row+1))
						{
							toolTip += p.toString() + " <br> ";
							paradaCount++;
						}
				}
				toolTip += "</html>";
				
				if(paradaCount == 0)
					toolTip = null;
			}
			
			planilha.setToolTipText( toolTip );
		}

       });
// end of testing
                       
	}

	private void setupHeaders()
	{
		GroupableTableColumnModel cm = (GroupableTableColumnModel)planilha.getColumnModel();
				
		// create groups
				
		//00 - 03
		ColumnGroup g_evt = new ColumnGroup("Evento");
		g_evt.add(cm.getColumn(0));
		g_evt.add(cm.getColumn(1));
		g_evt.add(cm.getColumn(2));		
		g_evt.add(cm.getColumn(3));
		cm.addColumnGroup(g_evt);
        
        //04 - 05
        ColumnGroup g_ac1 = new ColumnGroup("Aciaria 1");
        
        g_ac1.add(cm.getColumn(4));
        g_ac1.add(cm.getColumn(5));
        cm.addColumnGroup(g_ac1);

        //06 - 07
        ColumnGroup g_ac2 = new ColumnGroup("Aciaria 2");        
        g_ac2.add(cm.getColumn(6));
        g_ac2.add(cm.getColumn(7));
        cm.addColumnGroup(g_ac2);
        
        //14 - 17
        ColumnGroup g_afs = new ColumnGroup("Produção Gusa");
        g_afs.add(cm.getColumn(14));
        g_afs.add(cm.getColumn(15));
        g_afs.add(cm.getColumn(16));
        g_afs.add(cm.getColumn(17));
        cm.addColumnGroup(g_afs);
        
        //18-23
        ColumnGroup g_ref = new ColumnGroup("Refino Secundário Ac2");
        g_ref.add(cm.getColumn(18));
        g_ref.add(cm.getColumn(19));
        g_ref.add(cm.getColumn(20));
        g_ref.add(cm.getColumn(21));
        g_ref.add(cm.getColumn(22));
        g_ref.add(cm.getColumn(23));
        cm.addColumnGroup(g_ref);
        
        //24-25
        ColumnGroup g_fp1 = new ColumnGroup("Forno Panela Ac1");
        g_fp1.add(cm.getColumn(24));
        g_fp1.add(cm.getColumn(25));
        cm.addColumnGroup(g_fp1);
        
	// end of grouping of headers
	}

	public JScrollPane getScroll()
	{
		return this.scroll;
	}
	
	private void setRenderers()
	{
		planilha.getColumn( labels[ 0] ).setCellRenderer(Utils.createParadaRender(0, paradas));
        planilha.getColumn( labels[ 1] ).setCellRenderer(Utils.createParadaRender(1, paradas));
        planilha.getColumn( labels[ 2] ).setCellRenderer(Utils.createParadaRender(2, paradas));
        planilha.getColumn( labels[ 3] ).setCellRenderer(Utils.createParadaRender(3, paradas));
        
        planilha.getColumn( labels[ 4]).setCellRenderer(Utils.createNumberRenderer(false) ); // num corridas ac1
        planilha.getColumn( labels[ 5]).setCellRenderer(Utils.createNumberRenderer(false) ); // carga media ac1
        planilha.getColumn( labels[ 6]).setCellRenderer(Utils.createNumberRenderer(false) ); // num corridas ac2
        planilha.getColumn( labels[ 7]).setCellRenderer(Utils.createNumberRenderer(false) ); // carga media ac2
        planilha.getColumn( labels[ 8]).setCellRenderer(Utils.createNumberRenderer(false) ); 
        planilha.getColumn( labels[ 9]).setCellRenderer(Utils.createNumberRenderer(true)  ); // producao aco total
        planilha.getColumn( labels[10]).setCellRenderer(Utils.createNumberRenderer(false) ); 
        planilha.getColumn( labels[11]).setCellRenderer(Utils.createNumberRenderer(false) ); // sucata ac1
        planilha.getColumn( labels[12]).setCellRenderer(Utils.createNumberRenderer(false) ); // sucata ac2
        
        planilha.getColumn( labels[13]).setCellRenderer(Utils.createNumberRenderer(false) );
        planilha.getColumn( labels[14]).setCellRenderer(Utils.createNumberRenderer(false) );
        planilha.getColumn( labels[15]).setCellRenderer(Utils.createNumberRenderer(false) );
        planilha.getColumn( labels[16]).setCellRenderer(Utils.createNumberRenderer(false) );
        planilha.getColumn( labels[17]).setCellRenderer(Utils.createNumberRenderer(true)  );
        
        planilha.getColumn( labels[18]).setCellRenderer(Utils.createNumberRenderer(false) ); // hic
        planilha.getColumn( labels[19]).setCellRenderer(Utils.createNumberRenderer(false) ); // dr
        planilha.getColumn( labels[20]).setCellRenderer(Utils.createNumberRenderer(false) ); // rh
        planilha.getColumn( labels[21]).setCellRenderer(Utils.createNumberRenderer(false) ); // fp
        planilha.getColumn( labels[22]).setCellRenderer(Utils.createNumberRenderer(false) ); // rh total
        planilha.getColumn( labels[23]).setCellRenderer(Utils.createNumberRenderer(false) ); // fp total
        planilha.getColumn( labels[24]).setCellRenderer(Utils.createNumberRenderer(false) ); // fp total
        planilha.getColumn( labels[25]).setCellRenderer(Utils.createNumberRenderer(false) ); // fp total
	}

	private TableModel assembleModel()
	{
		return new TableModel()
		{
			@Override
			public void addTableModelListener(TableModelListener l){}

			@Override
			public Class<?> getColumnClass(int columnIndex)
			{
				return String.class;
			}

			@Override
			public int getColumnCount()
			{
				return labels.length;
			}

			@Override
			public String getColumnName(int columnIndex)
			{
				return labels[columnIndex];
			}

			@Override
			public int getRowCount()
			{				
				return numDias+1; // (+1) totals
			}

			@Override
			public Object getValueAt(int rowIndex, int columnIndex)
			{
				if(rowIndex < numDias)
				{
					if(hasData)
					{
						boolean isAltered = isAltered(columnIndex, rowIndex);
						
						if(columnIndex == 4)
							return new ValorLabel( corridas_ac1   [rowIndex], "", isAltered);
						if(columnIndex == 5)
							return new ValorLabel( carga_med_ac1  [rowIndex], "t/c", isAltered, 1);
						if(columnIndex == 6)
							return new ValorLabel( corridas_ac2   [rowIndex], "", isAltered);
						if(columnIndex == 7)
							return new ValorLabel( carga_med_ac2  [rowIndex], "t/c", isAltered, 1);
						if(columnIndex == 9)
							return new ValorLabel( producao_aco   [rowIndex], "t", isAltered);
						if(columnIndex == 10)
							return new ValorLabel( saldo_diario   [rowIndex], UNIT_TON, isAltered, new double[]{Constants.LIMITE_SALDO_BAIXO, Constants.LIMITE_SALDO_ALTO});
						if(columnIndex == 11)
							return new ValorLabel( perc_sucata_ac1[rowIndex], "%", isAltered, 1);
						if(columnIndex == 12)
							return new ValorLabel( perc_sucata_ac2[rowIndex], "%", isAltered, 1);						
						if(columnIndex == 13)
							return new ValorLabel( perc_sucata_total[rowIndex], "%", isAltered, 1);						
						if(columnIndex == 14)
							return new ValorLabel( prod_diaria_af1[rowIndex], UNIT_TON, isAltered);
						if(columnIndex == 15)
							return new ValorLabel( prod_diaria_af2[rowIndex], UNIT_TON, isAltered);
						if(columnIndex == 16)
							return new ValorLabel( prod_diaria_af3[rowIndex], UNIT_TON, isAltered);
						if(columnIndex == 17) 
							return new ValorLabel( prod_diaria_af1[rowIndex] + prod_diaria_af2[rowIndex] + prod_diaria_af3[rowIndex], UNIT_TON, isAltered);
						if(columnIndex == 18)
							return new ValorLabel( restr_hic   [rowIndex], "", isAltered);
						if(columnIndex == 19)
							return new ValorLabel( restr_dr    [rowIndex], "", isAltered);
						if(columnIndex == 20)
							return new ValorLabel( restr_rh    [rowIndex], "", isAltered);
						if(columnIndex == 21)
							return new ValorLabel( restr_fp2   [rowIndex], "", isAltered);
						if(columnIndex == 22)
							return new ValorLabel( restr_rh    [rowIndex] + restr_dr[rowIndex] + restr_hic[rowIndex], "", isAltered);
						if(columnIndex == 23) // fp tot
						{ 
							double carga_media_rest_fp2 = Constants.mainWindowHandle.getParametros().getParameter(EditorParametros.CM_AC2_FP2);
							double carga_media_rest_dr  = Constants.mainWindowHandle.getParametros().getParameter(EditorParametros.CM_AC2_DR);
							
							double fp_total = restr_fp2[rowIndex] * carga_media_rest_fp2 +
											  restr_dr [rowIndex] * carga_media_rest_dr;
							
							return new ValorLabel( fp_total, "", isAltered);
						}
						if(columnIndex == 24)
							return new ValorLabel( restr_fp1[rowIndex], "", isAltered);
						if(columnIndex == 25)
						{
							double carga_media_rest_fp1 = Constants.mainWindowHandle.getParametros().getParameter(EditorParametros.CM_AC1_FP1);							
							double fp_ac1 = restr_fp1[rowIndex] * carga_media_rest_fp1;
							return new ValorLabel( fp_ac1, "", isAltered);
						}
						
						
						
						
					} // end of has data
				} else { // totals

					if(columnIndex == 4)
						return new ValorLabel( corridas_ac1_total, "", false);
					if(columnIndex == 5)
						return new ValorLabel( carga_med_ac1_total, "t/c", false, 1);
					if(columnIndex == 6)
						return new ValorLabel( corridas_ac2_total, "", false);
					if(columnIndex == 7)
						return new ValorLabel( carga_med_ac2_total, "t/c", false, 1);
					if(columnIndex == 9)
						return new ValorLabel( producao_aco_total, UNIT_TON, false);
					
					if(columnIndex == 11)
						return new ValorLabel( suc_ac1_total, "%", false, 1);
					if(columnIndex == 12)
						return new ValorLabel( suc_ac2_total, "%", false, 1);
					if(columnIndex == 13)
						return new ValorLabel( suc_total, "%", false, 1);
						
					if(columnIndex == 14)
						return new ValorLabel( prod_diaria_af1_total, UNIT_TON, false);
					if(columnIndex == 15)
						return new ValorLabel( prod_diaria_af2_total, UNIT_TON, false);
					if(columnIndex == 16)
						return new ValorLabel( prod_diaria_af3_total, UNIT_TON, false);
					if(columnIndex == 17)
						return new ValorLabel( prod_diaria_af1_total+prod_diaria_af2_total+prod_diaria_af3_total, UNIT_TON, false);	

					if(columnIndex == 18)
						return new ValorLabel( restr_hic_total, "", false);
					if(columnIndex == 19)
						return new ValorLabel( restr_dr_total, "", false);
					if(columnIndex == 20)
						return new ValorLabel( restr_rh_total, "", false);
					if(columnIndex == 21)
						return new ValorLabel( restr_fp2_total, "", false);
					if(columnIndex == 22)
						return new ValorLabel( restr_hic_total + restr_rh_total + restr_dr_total, "", false);
					if(columnIndex == 23) // fp tot
					{ 
						double carga_media_rest_fp2 = Constants.mainWindowHandle.getParametros().getParameter(EditorParametros.CM_AC2_FP2);
						double carga_media_rest_dr  = Constants.mainWindowHandle.getParametros().getParameter(EditorParametros.CM_AC2_DR);
						
						double fp_total = restr_fp2_total * carga_media_rest_fp2 +
										  restr_dr_total  * carga_media_rest_dr;
						
						return new ValorLabel( fp_total, "", false);
					}
					if(columnIndex == 24)
						return new ValorLabel( restr_fp1_total, "", false);
					if(columnIndex == 25)
					{
						double carga_media_rest_fp1 = Constants.mainWindowHandle.getParametros().getParameter(EditorParametros.CM_AC1_FP1);							
						double fp_ac1 = restr_fp1_total * carga_media_rest_fp1;
						return new ValorLabel( fp_ac1, "", false);
					}
					
					
				}
				return null;
			}

			private boolean isAltered(int columnIndex, int rowIndex)
			{
				for(AlteracaoManual am:alteracoes)
				{
					if(am.getParametro() == columnIndex)
						if(am.getRow() == rowIndex)
							return true;
				}
				return false;
			}

			@Override
			public boolean isCellEditable(int rowIndex, int columnIndex)
			{
				if(columnIndex >= 14 && columnIndex <= 16) // prod af's
					return true;
				
				return false;
			}

			@Override
			public void removeTableModelListener(TableModelListener l){}

			@Override
			public void setValueAt(Object aValue, int rowIndex, int columnIndex)
			{
				double newValue = -1.0;
				try {
					
					newValue = Double.parseDouble(aValue.toString());
					
				} catch(NumberFormatException e) {
					return;
				}
				
				if(newValue > -1.0)
				{
					AlteracaoManual alteracao = new AlteracaoManual(columnIndex, rowIndex+1, newValue);
					alteracoes.add(alteracao);
					processarAlteracoes();
				}				
			}			
		};
	}
	
	private int computeShift()
	{
		Calendar c = Calendar.getInstance();
		c.setTime(periodo);
		int diaSemana = c.get(Calendar.DAY_OF_WEEK);
		
		return diaSemana-1;
	}

	private int computeNumDias()
	{
		Calendar cal = Calendar.getInstance();
		cal.setTime(periodo);
				
		return cal.getActualMaximum(Calendar.DAY_OF_MONTH);
	}
	public int getNumDias()
	{
		return this.numDias;
	}
	
	public ArrayList<Parada> getParadas()
	{
		return paradas;
	}			
	

//                AAA                 QQQQQQQQQ     UUUUUUUU     UUUUUUUUIIIIIIIIII
//               A:::A              QQ:::::::::QQ   U::::::U     U::::::UI::::::::I
//              A:::::A           QQ:::::::::::::QQ U::::::U     U::::::UI::::::::I
//             A:::::::A         Q:::::::QQQ:::::::QUU:::::U     U:::::UUII::::::II
//            A:::::::::A        Q::::::O   Q::::::Q U:::::U     U:::::U   I::::I  
//           A:::::A:::::A       Q:::::O     Q:::::Q U:::::D     D:::::U   I::::I  
//          A:::::A A:::::A      Q:::::O     Q:::::Q U:::::D     D:::::U   I::::I  
//         A:::::A   A:::::A     Q:::::O     Q:::::Q U:::::D     D:::::U   I::::I  
//        A:::::A     A:::::A    Q:::::O     Q:::::Q U:::::D     D:::::U   I::::I  
//       A:::::AAAAAAAAA:::::A   Q:::::O     Q:::::Q U:::::D     D:::::U   I::::I  
//      A:::::::::::::::::::::A  Q:::::O  QQQQ:::::Q U:::::D     D:::::U   I::::I  
//     A:::::AAAAAAAAAAAAA:::::A Q::::::O Q::::::::Q U::::::U   U::::::U   I::::I  
//    A:::::A             A:::::AQ:::::::QQ::::::::Q U:::::::UUU:::::::U II::::::II
//   A:::::A               A:::::AQQ::::::::::::::Q   UU:::::::::::::UU  I::::::::I
//  A:::::A                 A:::::A QQ:::::::::::Q      UU:::::::::UU    I::::::::I
// AAAAAAA                   AAAAAAA  QQQQQQQQ::::QQ      UUUUUUUUU      IIIIIIIIII
//                                            Q:::::Q                              
//                                             QQQQQQ                              	
// the hard core
	public void calcular(boolean redoHotMetal)
	{		
		if(this.provider == null)
		{
			System.out.println("Não há provedor de solução");
			return;
		}
		
		Constants.mainWindowHandle.print2Console("Iniciando calculo.");

		int hic_dec1 = (int) Constants.mainWindowHandle.getInserirDemanda().getDemanda(InserirDemanda.HIC, 1);
		int hic_dec2 = (int) Constants.mainWindowHandle.getInserirDemanda().getDemanda(InserirDemanda.HIC, 2);
		int hic_dec3 = (int) Constants.mainWindowHandle.getInserirDemanda().getDemanda(InserirDemanda.HIC, 3);
		int demanda_hic = hic_dec1+hic_dec2+hic_dec3;
		
		int dr_dec1 = (int) Constants.mainWindowHandle.getInserirDemanda().getDemanda(InserirDemanda.DR, 1);
		int dr_dec2 = (int) Constants.mainWindowHandle.getInserirDemanda().getDemanda(InserirDemanda.DR, 2);
		int dr_dec3 = (int) Constants.mainWindowHandle.getInserirDemanda().getDemanda(InserirDemanda.DR, 3);
		int demanda_dr = dr_dec1+dr_dec2+dr_dec3;

		int rh_dec1 = (int) Constants.mainWindowHandle.getInserirDemanda().getDemanda(InserirDemanda.RH, 1);
		int rh_dec2 = (int) Constants.mainWindowHandle.getInserirDemanda().getDemanda(InserirDemanda.RH, 2);
		int rh_dec3 = (int) Constants.mainWindowHandle.getInserirDemanda().getDemanda(InserirDemanda.RH, 3);
		int demanda_rh = rh_dec1+rh_dec2+rh_dec3;

		int fp_dec1 = (int) Constants.mainWindowHandle.getInserirDemanda().getDemanda(InserirDemanda.FP, 1);
		int fp_dec2 = (int) Constants.mainWindowHandle.getInserirDemanda().getDemanda(InserirDemanda.FP, 2);
		int fp_dec3 = (int) Constants.mainWindowHandle.getInserirDemanda().getDemanda(InserirDemanda.FP, 3);
		int demanda_fp = fp_dec1+fp_dec2+fp_dec3;
		
		// obtain data from provider
				this.corridas_ac1     = provider.corridas_ac1();
				this.restr_hic        = provider.restr_hic();
				this.restr_dr         = provider.restr_dr();
				this.restr_rh         = provider.restr_rh();
				this.restr_fp1        = provider.restr_fp1();
				this.restr_fp2        = provider.restr_fp2();		
		// end of provider
							
	if(redoHotMetal)
	{			
			double std_af1 = EditorParametros.values[ EditorParametros.PROD_AF1 ];
			double std_af2 = EditorParametros.values[ EditorParametros.PROD_AF2 ];
			double std_af3 = EditorParametros.values[ EditorParametros.PROD_AF3 ];
					
			for(int i=0; i<numDias; i++)
			{
				// hot metal production
				prod_diaria_af1[i] = Math.rint( std_af1 * indiceFuncionamentoDia(Equip.AF1, i+1) );
				prod_diaria_af2[i] = Math.rint( std_af2 * indiceFuncionamentoDia(Equip.AF2, i+1) );
				prod_diaria_af3[i] = Math.rint( std_af3 * indiceFuncionamentoDia(Equip.AF3, i+1) );
			}
			
			// processando alterações manuais
			processarAlteracoes();
	
			for(int i=0; i<numDias; i++)
			{
				// totals
				prod_diaria_af1_total += prod_diaria_af1[i];
				prod_diaria_af2_total += prod_diaria_af2[i];
				prod_diaria_af3_total += prod_diaria_af3[i];
			}
	}
		
		boolean calcularCapacidadeVerbose = true;
		this.corridas_ac2 = calcularCapacidadeAciaria2(calcularCapacidadeVerbose);		
		
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////		
//
//   uma vez calculada a capacidade, iterar dia a dia olhando o saldo de gusa
//		
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////		
		double perda_gusa = Constants.mainWindowHandle.getParametros().getParameter(EditorParametros.PERDA_GUSA);
		
// aciaria 1
		double cm_ac1_comum    = Constants.mainWindowHandle.getParametros().getParameter(EditorParametros.CM_AC1_COMUM);
		double cm_ac1_fp1      = Constants.mainWindowHandle.getParametros().getParameter(EditorParametros.CM_AC1_FP1);
		double suc_ac1_comum   = Constants.mainWindowHandle.getParametros().getParameter(EditorParametros.PSUC_AC1_COMUM);
		double suc_ac1_fp1     = Constants.mainWindowHandle.getParametros().getParameter(EditorParametros.PSUC_AC1_FP1);
		double rend_ac1        = Constants.mainWindowHandle.getParametros().getParameter(EditorParametros.REND_AC1);
	
// aciaria 2
		double rend_ac2        = Constants.mainWindowHandle.getParametros().getParameter(EditorParametros.REND_AC2       );
		double suc_ac2_hic     = Constants.mainWindowHandle.getParametros().getParameter(EditorParametros.PSUC_AC2_HIC   );
		double suc_ac2_dr      = Constants.mainWindowHandle.getParametros().getParameter(EditorParametros.PSUC_AC2_DR    );
		double suc_ac2_rh      = Constants.mainWindowHandle.getParametros().getParameter(EditorParametros.PSUC_AC2_RH    );
		double suc_ac2_fp2     = Constants.mainWindowHandle.getParametros().getParameter(EditorParametros.PSUC_AC2_FP    );
		double suc_ac2_comum   = Constants.mainWindowHandle.getParametros().getParameter(EditorParametros.PSUC_AC2_COMUM );
		double cm_ac2_hic      = Constants.mainWindowHandle.getParametros().getParameter(EditorParametros.CM_AC2_HIC     );
		double cm_ac2_dr       = Constants.mainWindowHandle.getParametros().getParameter(EditorParametros.CM_AC2_DR      );
		double cm_ac2_rh       = Constants.mainWindowHandle.getParametros().getParameter(EditorParametros.CM_AC2_RH      );
		double cm_ac2_fp2      = Constants.mainWindowHandle.getParametros().getParameter(EditorParametros.CM_AC2_FP2     );
		double cm_ac2_comum    = Constants.mainWindowHandle.getParametros().getParameter(EditorParametros.CM_AC2_COMUM   );
		
		boolean calcularSucata1 = true,
				calcularSucata2 = true;
		
		zerarTotais();
		for(int i=0; i<numDias; i++)
		{ 
		// aciaria 1
			carga_med_ac1   [i] = corridas_ac1[i] == 0 ? 0.0 :      ((corridas_ac1[i]-restr_fp1[i])* cm_ac1_comum + (restr_fp1[i]*cm_ac1_fp1 ))/corridas_ac1[i];
			
		if(calcularSucata1)
		{
			perc_sucata_ac1 [i] = corridas_ac1[i] == 0 ? 0.0 : 100*(((corridas_ac1[i]-restr_fp1[i])*suc_ac1_comum + (restr_fp1[i]*suc_ac1_fp1))/corridas_ac1[i]);
		}
			consumo_gusa_ac1[i] = corridas_ac1[i] == 0 ? 0.0 :
                                 (corridas_ac1[i]*carga_med_ac1[i]/rend_ac1)*(1-(perc_sucata_ac1[i]/100));				
		// aciaria 2
			int corridas_comuns = corridas_ac2[i] - restr_hic[i] - restr_dr[i] - restr_fp2[i] - restr_rh[i];
			
			carga_med_ac2   [i] = corridas_ac2[i] == 0 ? 0.0 :
								 (corridas_comuns * cm_ac2_comum +
								  restr_hic[i]    * cm_ac2_hic   +
								  restr_dr [i]    * cm_ac2_dr    +
								  restr_fp2[i]    * cm_ac2_fp2   +
								  restr_rh [i]    * cm_ac2_rh    )/corridas_ac2[i];
			if(calcularSucata2)
			{
				perc_sucata_ac2 [i] = corridas_ac2[i] == 0 ? 0.0 :
							    100*((corridas_comuns * suc_ac2_comum +
									  restr_hic[i]    * suc_ac2_hic   +
									  restr_dr [i]    * suc_ac2_dr    +
									  restr_fp2[i]    * suc_ac2_fp2   +
									  restr_rh [i]    * suc_ac2_rh    )/corridas_ac2[i]);
			}
			
			consumo_gusa_ac2[i] = corridas_ac2[i] == 0 ? 0.0 :
				                  (corridas_ac2[i]*carga_med_ac2[i]/rend_ac2)*(1-(perc_sucata_ac2[i]/100));
		// valores consolidados
			producao_aco_ac1[i] = carga_med_ac1[i]*corridas_ac1[i];
			producao_aco_ac2[i] = carga_med_ac2[i]*corridas_ac2[i];
			producao_aco    [i] = producao_aco_ac1[i] + producao_aco_ac2[i];
			perc_sucata_total[i] = (perc_sucata_ac1[i]*producao_aco_ac1[i] + perc_sucata_ac2[i]*producao_aco_ac2[i])/(producao_aco[i]);		


// calculo do saldo de gusa
			saldo_diario[i] = (i==0 ? EditorParametros.values[ EditorParametros.SALDO_INI ] : saldo_diario[i-1]) 
					+ ((prod_diaria_af1[i] + prod_diaria_af2[i] + prod_diaria_af3[i])*(1-perda_gusa))
					- consumo_gusa_ac1[i]
					- consumo_gusa_ac2[i];
// fim do cálculo do saldo de gusa
			
		// acerto do saldo de gusa
			if(saldo_diario[i] < Constants.LIMITE_SALDO_BAIXO)
			{
				if(corridas_ac2[i]>1)
				{
					corridas_ac2[i]--;
					i--;
					continue;
				}
			}
			else
				if(saldo_diario[i] > Constants.LIMITE_SALDO_ALTO)
				{
					if(perc_sucata_ac1[i] > Constants.MIN_SUCATA_AC1/0.95)
					{
						perc_sucata_ac1[i] *= 0.95;
						calcularSucata1 = false;
					}
					
					if(perc_sucata_ac2[i] > Constants.MIN_SUCATA_AC2/0.95)
					{
						perc_sucata_ac2[i] *= 0.95;
						calcularSucata2 = false;
					}
					else
						corridas_ac2[i]++;

					i--;
					
					continue;
				}
			
			calcularSucata1 = true;
			calcularSucata2 = true;
			
			// totais (só passar aqui após acerto do saldo)						
			corridas_ac1_total  += corridas_ac1[i];
			corridas_ac2_total  += corridas_ac2[i];
			carga_med_ac1_total += corridas_ac1[i]*carga_med_ac1[i];
			carga_med_ac2_total += corridas_ac2[i]*carga_med_ac2[i];
			restr_hic_total     += restr_hic   [i];
			restr_dr_total      += restr_dr    [i];
			restr_rh_total      += restr_rh    [i];
			restr_fp1_total     += restr_fp1   [i];
			restr_fp2_total     += restr_fp2   [i];
			producao_aco_total  += producao_aco[i];			
			producao_aco_ac1_total += producao_aco_ac1[i];
			producao_aco_ac2_total += producao_aco_ac2[i];
			suc_ac1_total       += producao_aco_ac1[i] * perc_sucata_ac1[i];
			suc_ac2_total       += producao_aco_ac2[i] * perc_sucata_ac2[i];
			suc_total           += producao_aco[i]     * perc_sucata_total[i];	
		}
		
		// totais 
		carga_med_ac1_total /= corridas_ac1_total;
		carga_med_ac2_total /= corridas_ac2_total;
		suc_ac1_total       /= producao_aco_ac1_total;
		suc_ac2_total       /= producao_aco_ac2_total;
		suc_total           /= producao_aco_total;		
		
		this.hasData = true;
		
// computing the fitness function		
		double fitness = 0;

		double f_hic = Utils.myGaussian( restr_hic_total  - demanda_hic),
		       f_dr  = Utils.myGaussian( restr_dr_total   - demanda_dr) ,
		       f_rh  = Utils.myGaussian( restr_rh_total   - demanda_rh) ,
		       f_fp  = Utils.myGaussian( restr_fp2_total  - demanda_fp); 
		
		fitness = f_hic +5*f_dr+f_rh+f_fp + producao_aco_total;
		
		setFitness( fitness );
// end of fitness adjustment
	
		String message = String.format("Cálculo terminado: fitness=%.4f", this.fitnessValue);		
		Constants.mainWindowHandle.print2Console(message);
		
repaint();
		
		if(redoHotMetal) repaint();
	}
	
/////////////////////////////////////////////////////////////////////////////////////////
//  
//		FIM DO MÉTODO CALCULAR()
//
/////////////////////////////////////////////////////////////////////////////////////////	
	
	
	
	
	
	
	
	
	
	
	
	
	
	private void processarAlteracoes()
	{
		for(AlteracaoManual am:alteracoes)
		{
			if(am.getParametro() == 14) // producao af1
				prod_diaria_af1[am.getRow()] = am.getValor();
			if(am.getParametro() == 15) // producao af2
				prod_diaria_af2[am.getRow()] = am.getValor();
			if(am.getParametro() == 16) // producao af3
				prod_diaria_af3[am.getRow()] = am.getValor();
		}
		
	}

	public void inserirParada(Parada parada)
	{
		// checks if this parada is incident on the same day
		if( hasParadaEquipDia(parada.getEquipamento(), parada.getMinDay(), parada.getMaxDay()) )
		{
			String title = "Falha ao inserir parada";
			String message = "Já existe uma parada para este equipamento no mesmo período.\nSe necessário remova a parada existente ou edite sua duração.";
			
			JOptionPane.showMessageDialog(null, message, title, JOptionPane.ERROR_MESSAGE);
			return;
		}

		parada.setColorIndex( paradas.size() + 1 );
		paradas.add(parada);
		
		//System.out.println( parada );
		repaint();
	}
	
	// looks for an existent parada for some equipment some day
	public boolean hasParadaEquipDia(Equip equip, int from, int to)
	{
		for(int i=from; i<to+1; i++)
		{
			for(Parada p : paradas)
			{
				if( p.getEquipamento() == equip )
					if( p.occursInDay(i) )
						return true;
			}
		}
		return false;
	}

	public double indiceFuncionamentoDia(Equip equipamento, int dia)
	{
		for(Parada p : paradas)
		{
			if(p.getEquipamento() == equipamento)
				if(p.occursInDay(dia))
				{
					double percParado = p.getRangeDay(dia)[1];
					return 1 - percParado;
				}
		}
		
		return 1.0;
	}
	
	public void limparParadas()
	{
		this.paradas.clear();
	}
	
	public void carregarParadas(ArrayList<Parada> paradas)
	{
		this.paradas.clear();
		this.paradas.addAll(paradas);
		update();
	}
	
	public void update()
	{
		planilha.setModel( assembleModel() );
		setupHeaders();
        setRenderers();
	}

	public void inserirAlteracaoManual(AlteracaoManual alteracao)
	{
		alteracoes.add(alteracao);
	}
		
	public ArrayList<AlteracaoManual> getAlteracoesManuais()
	{
		return alteracoes;
	}
	
	public Iterator<String> getPlanoSQLInserts()
	{
		ArrayList<String> lista = new ArrayList<String>();
/*	
	dia 			integer,
	corr_ac1 		integer,
	carga_ac1 		real,
	corr_ac2 		integer,
	carga_ac2 		real,
	corr_transf 	integer,
	producao_aco 	real,	
	saldo 			real,
	psuc_ac1 		real,
	psuc_ac2 		real,
	psuc_tot	 	real,	
	prod_af1 		real,
	prod_af2 		real,
	prod_af3 		real,
	prod_af 		real,
	restr_hic 		integer,
	restr_dr 		integer,
	restr_rh 		integer,
	restr_fp2 		integer,
	aco_rh_tot 		real,
	aco_fp2_tot 	real,
	restr_fp1 		integer,
	aco_fp1_tot 	real,	
 */
		String formatString = "insert into plano (%s) values (%d, %d, %s, %d, %s, %d, %s, %s, %s, %s, %s, %s, %s, %s, %s, %d, %d, %d, %d, %s, %s, %d, %s)";		
		for(int i=0; i<numDias;i++) 
		{
			double aco_rh_total  = Constants.mainWindowHandle.getParametros().getParameter(EditorParametros.CM_AC2_RH)  * restr_rh[i] 
					             + Constants.mainWindowHandle.getParametros().getParameter(EditorParametros.CM_AC2_DR)  * restr_dr[i], 
			       aco_fp2_total = Constants.mainWindowHandle.getParametros().getParameter(EditorParametros.CM_AC2_FP2) * restr_fp2[i]
			    		   		 + Constants.mainWindowHandle.getParametros().getParameter(EditorParametros.CM_AC2_DR)  * restr_dr[i],
			       aco_fp1_total = Constants.mainWindowHandle.getParametros().getParameter(EditorParametros.CM_AC1_FP1) * restr_fp1[i];
			
			String insert = String.format(formatString
					,FIELD_LIST
/* dia */			,i+1
					,corridas_ac1[i]
					,Utils.strDouble( carga_med_ac1[i] )
					,corridas_ac2[i] 		
					,Utils.strDouble( carga_med_ac2[i] )
/* transferencia */	,0
					,Utils.strDouble( producao_aco[i] )					
					,Utils.strDouble( saldo_diario[i] )
					,Utils.strDouble( perc_sucata_ac1[i] )
					,Utils.strDouble( perc_sucata_ac2[i] )
					,Utils.strDouble( perc_sucata_total[i] )					
					,Utils.strDouble( prod_diaria_af1[i] )
					,Utils.strDouble( prod_diaria_af2[i] )
					,Utils.strDouble( prod_diaria_af3[i] )
					,Utils.strDouble( prod_diaria_af1[i]+prod_diaria_af2[i]+prod_diaria_af3[i] )
					,restr_hic[i]
					,restr_dr[i]
					,restr_rh[i]
					,restr_fp2[i]
					,Utils.strDouble( aco_rh_total )
					,Utils.strDouble( aco_fp2_total )
					,restr_fp1[i]
					,Utils.strDouble( aco_fp1_total )
					);
			lista.add(insert);			
		}
		
		double aco_rh_total  = Constants.mainWindowHandle.getParametros().getParameter(EditorParametros.CM_AC2_RH)  * restr_rh_total 
	             			 + Constants.mainWindowHandle.getParametros().getParameter(EditorParametros.CM_AC2_DR)  * restr_dr_total, 
	           aco_fp2_total = Constants.mainWindowHandle.getParametros().getParameter(EditorParametros.CM_AC2_FP2) * restr_fp2_total
		   		             + Constants.mainWindowHandle.getParametros().getParameter(EditorParametros.CM_AC2_DR)  * restr_dr_total,
		   	   aco_fp1_total = Constants.mainWindowHandle.getParametros().getParameter(EditorParametros.CM_AC1_FP1) * restr_fp1_total;

		
		// include a line with totals
		lista.add(String.format(formatString
				,FIELD_LIST
/* dia */		,numDias+1
				,corridas_ac1_total
				,Utils.strDouble( carga_med_ac1_total )
				,corridas_ac2_total		
				,Utils.strDouble( carga_med_ac2_total )
/* transf */	,0
				,Utils.strDouble( producao_aco_total )					
/* saldo*/		,"0.0"
				,Utils.strDouble( suc_ac1_total )
				,Utils.strDouble( suc_ac2_total )
				,Utils.strDouble( suc_total )					
				,Utils.strDouble( prod_diaria_af1_total )
				,Utils.strDouble( prod_diaria_af2_total )
				,Utils.strDouble( prod_diaria_af3_total )
				,Utils.strDouble( prod_diaria_af1_total+prod_diaria_af2_total+prod_diaria_af3_total )
				,restr_hic_total
				,restr_dr_total
				,restr_rh_total
				,restr_fp2_total
				,Utils.strDouble( aco_rh_total )
				,Utils.strDouble( aco_fp2_total )
				,restr_fp1_total
				,Utils.strDouble( aco_fp1_total )
		));
		
		return lista.iterator();
	}
	
	public boolean hasData()
	{
		return hasData;
	}
	public void setHasData(boolean hasData)
	{
		this.hasData = hasData;
	}
	
	public void insertDemanda(int aco, int[] values)
	{
		double[] doubles = new double[values.length];
		for(int i=0; i<values.length; i++)
			doubles[i] = (double) values[i];
		inserirDemanda.setValues(aco, doubles);
	}
	
	public void setCalculatedValues(int dia, double[] values)
	{
		int row = dia-1;
		// up to now, there are 4 columns of data
		if(values.length == FIELD_LIST_SIZE-1)
		{
			if(row < numDias)
			{
				corridas_ac1      [row] = (int) values[ 0];
				carga_med_ac1     [row] =       values[ 1];
				corridas_ac2      [row] = (int) values[ 2];
				carga_med_ac2     [row] =       values[ 3];
				// transf.
				producao_aco      [row] =       values[ 5];
				saldo_diario      [row] =       values[ 6];
				perc_sucata_ac1   [row] =       values[ 7];
				perc_sucata_ac2   [row] =       values[ 8];
				perc_sucata_total [row] =       values[ 9];				
				prod_diaria_af1   [row] =       values[10];
				prod_diaria_af2   [row] =       values[11];
				prod_diaria_af3   [row] =       values[12];
				// prod af total é calculada
				restr_hic         [row] = (int) values[14];
				restr_dr          [row] = (int) values[15];
				restr_rh          [row] = (int) values[16];
				restr_fp2         [row] = (int) values[17];
				// aco rh total
				// aco fp2 total
				restr_fp1         [row] = (int) values[20];
				// aco fp2 total				
			} 
			if(row == numDias)
			{
				corridas_ac1_total      = (int) values[ 0];
				carga_med_ac1_total     =       values[ 1];
				corridas_ac2_total      = (int) values[ 2];
				carga_med_ac2_total     =       values[ 3];
				// transf.
				producao_aco_total      =       values[ 5];
				// saldo final                              
				suc_ac1_total           =       values[ 7];
				suc_ac2_total           =       values[ 8];
				suc_total               =       values[ 9];				
				prod_diaria_af1_total   =       values[10];
				prod_diaria_af2_total   =       values[11];
				prod_diaria_af3_total   =       values[12];
				// prod af total é calculada
				restr_hic_total         = (int) values[14];
				restr_dr_total          = (int) values[15];
				restr_rh_total          = (int) values[16];
				restr_fp2_total         = (int) values[17];
				// aco rh total
				// aco fp2 total
				restr_fp1_total         = (int) values[20];
				// aco fp2 total	
			}
		}
	}

	public void showInserirDemanda()
	{
		inserirDemanda.setVisible(true);
	}
	public InserirDemanda getInserirDemanda()
	{
		return inserirDemanda;
	}

// this is a sort of a macro, for testing purposes	
	public double[][] computeAvailabilityMatrix(boolean verbose)
	{
		if(!hasData)
			return null;
		
		double[][] availability = new double[numDias][5+3]; //5-Aciaria 2 + 3-Aciaria 1
		
	Constants.mainWindowHandle.print2Console("Inicio de unidade teste");
	
		for(int i=0;i<numDias;i++)
		{
			double parada_ac  = indiceFuncionamentoDia(Equip.ACIARIA_2, i+1);
			double indice_ld  = indiceFuncionamentoDia(Equip.CONV_AC2,  i+1);
			double indice_ob  = indiceFuncionamentoDia(Equip.CAS,       i+1);
			double indice_fp  = indiceFuncionamentoDia(Equip.FP2,       i+1);
			double indice_rh2 = indiceFuncionamentoDia(Equip.RH2,       i+1),
				   indice_rh3 = indiceFuncionamentoDia(Equip.RH3,       i+1),
				   indice_rh  = (indice_rh2+indice_rh3)/2.0;
			double indice_ml1 = indiceFuncionamentoDia(Equip.MLC1,      i+1),
				   indice_ml2 = indiceFuncionamentoDia(Equip.MLC2,      i+1),
				   indice_ml  = (indice_ml1+indice_ml2)/2.0;
			
			if(parada_ac < indice_ld)
				indice_ld = parada_ac;										

			if(parada_ac < indice_ob)
				indice_ob = parada_ac;										

			if(parada_ac < indice_fp)
				indice_fp = parada_ac;										

			if(parada_ac < indice_rh)
				indice_rh = parada_ac;										

			if(parada_ac < indice_ml)
				indice_ml = parada_ac;										
// aciaria 1
			double parada_ac1  = indiceFuncionamentoDia(Equip.ACIARIA_1, i+1);
			double indice_ld1  = indiceFuncionamentoDia(Equip.CONV_AC1,  i+1);
			double indice_fp1  = indiceFuncionamentoDia(Equip.FP1,       i+1);
			double indice_ml4  = indiceFuncionamentoDia(Equip.MLC4,      i+1);

			if(parada_ac1 < indice_ld1)
				indice_ld1 = parada_ac1;										

			if(parada_ac1 < indice_fp1)
				indice_fp1 = parada_ac1;										

			if(parada_ac1 < indice_ml4)
				indice_ml4 = parada_ac;										
// final aciaria 1
			
			if(verbose)
			{
				String result = String.format("dia %2d LD=%.4f  OB=%.4f  FP=%.4f  RH=%.4f  ML=%.4f", i+1, 
						indice_ld, indice_ob, indice_fp, indice_rh, indice_ml);
				// esta saída não está exibindo indices da aciaria 1
				Constants.mainWindowHandle.print2Console(result);
			}
			
			availability[i] = new double[]{indice_ld, indice_ob, indice_fp, indice_rh, indice_ml, indice_ld1, indice_fp1, indice_ml4};
		}
	
		return availability;
	}
	
	private void zerarTotais()
	{
	// doubles
		 prod_diaria_af1_total  = 0.0;
		 prod_diaria_af2_total	= 0.0;
		 prod_diaria_af3_total	= 0.0;
		 carga_med_ac1_total    = 0.0;
		 carga_med_ac2_total    = 0.0;
		 producao_aco_total		= 0.0;
		 producao_aco_ac1_total = 0.0;
		 producao_aco_ac2_total = 0.0;
		 suc_ac1_total          = 0.0;
		 suc_ac2_total          = 0.0;
		 suc_total              = 0.0;
	 // ints
		 corridas_ac1_total		= 0;
		 corridas_ac2_total		= 0;
		 restr_hic_total		= 0;
		 restr_dr_total			= 0;
		 restr_rh_total			= 0;
		 restr_fp1_total		= 0;
		 restr_fp2_total		= 0;
	}

	public int[] calcularCapacidadeAciaria2(boolean verbose)
	{		 
		
		double[][] aMatrix = computeAvailabilityMatrix(false);
		
		int[] capacidade = new int[numDias];
		
		int[] y  = provider.corridas_ac2();
		int[] dr = provider.restr_dr(),
			  rh = provider.restr_rh(),
			  fp = provider.restr_fp2();		
		
		for(int i=0; i<numDias; i++)
		{
			int y_bar = CalculoCapacidade.capacidade(dr[i], rh[i], fp[i], aMatrix[i]);
			
			if(verbose)
			{
				String str = String.format("Dia %2d Túlio=%2d Hiuller=%2d", i+1, y[i], y_bar);
				Constants.mainWindowHandle.print2Console(str);
			}
			
			capacidade[i] = y_bar;
		}
		
		return capacidade;
	}

	public void runTest()
	{
		ClasseDia cls = new ClasseDia(this);
		cls.spit();
	}
	
	private void setFitness(double value)
	{
		if(value < 1.0)
			this.fitnessValue = 1.0;
		else
			this.fitnessValue = value;
	}
	
	public double getFitness()
	{
		return this.fitnessValue;
	}

	public void limpar()
	{
		zerarTotais();
		for(int i=0; i<numDias; i++)
		{
			 corridas_ac1[i] = 0;
			 corridas_ac2[i] = 0;
			 restr_hic   [i] = 0;
			 restr_dr    [i] = 0;
			 restr_rh    [i] = 0;
			 restr_fp1   [i] = 0;
		 	 restr_fp2   [i] = 0;

		 	 saldo_diario[i] = 0.0;
		 	 
		 	 carga_med_ac1     [i] = 0.0;
			 perc_sucata_ac1   [i] = 0.0;
			 perc_sucata_ac2   [i] = 0.0;
			 perc_sucata_total [i] = 0.0;
			 consumo_gusa_ac1  [i] = 0.0;
             carga_med_ac2     [i] = 0.0;	 
			 consumo_gusa_ac2  [i] = 0.0;
			 producao_aco      [i] = 0.0;
			 producao_aco_ac1  [i] = 0.0;
			 producao_aco_ac2  [i] = 0.0;
		}
		update();
	}
	
	public void setSolutionProvider(SolutionProvider provider)
	{
		this.provider = provider;
	}
	
	public void clearSolutionProvider()
	{
		this.provider = null;
	}

	/**
	 * 
	 * @return a <code>Iterator</code> with the lines to be saved in the CSV file.
	 */
	public Iterator<String> getCSViterator()
	{
		ArrayList<String> result = new ArrayList<String>();
		
		String formatString = "%d, %d, %s, %d, %s, %d, %s, %s, %s, %s, %s, %s, %s, %s, %s, %d, %d, %d, %d, %s, %s, %d, %s\n";		
				
		for(int i=0; i<numDias;i++)
		{
			
			double aco_rh_total  = Constants.mainWindowHandle.getParametros().getParameter(EditorParametros.CM_AC2_RH)  * restr_rh[i] 
								 + Constants.mainWindowHandle.getParametros().getParameter(EditorParametros.CM_AC2_DR)  * restr_dr[i], 
				   aco_fp2_total = Constants.mainWindowHandle.getParametros().getParameter(EditorParametros.CM_AC2_FP2) * restr_fp2[i]
   		   		                 + Constants.mainWindowHandle.getParametros().getParameter(EditorParametros.CM_AC2_DR)  * restr_dr[i],
                                   aco_fp1_total = Constants.mainWindowHandle.getParametros().getParameter(EditorParametros.CM_AC1_FP1) * restr_fp1[i];
			
			String insert = String.format(formatString
/* dia */			,i+1
					,corridas_ac1[i]
					,Utils.strDouble( carga_med_ac1[i] )
					,corridas_ac2[i] 		
					,Utils.strDouble( carga_med_ac2[i] )
/* transferencia */	,0
					,Utils.strDouble( producao_aco[i] )					
					,Utils.strDouble( saldo_diario[i] )
					,Utils.strDouble( perc_sucata_ac1[i] )
					,Utils.strDouble( perc_sucata_ac2[i] )
					,Utils.strDouble( perc_sucata_total[i] )					
					,Utils.strDouble( prod_diaria_af1[i] )
					,Utils.strDouble( prod_diaria_af2[i] )
					,Utils.strDouble( prod_diaria_af3[i] )
					,Utils.strDouble( prod_diaria_af1[i]+prod_diaria_af2[i]+prod_diaria_af3[i] )
					,restr_hic[i]
					,restr_dr[i]
					,restr_rh[i]
					,restr_fp2[i]
					,Utils.strDouble( aco_rh_total )
					,Utils.strDouble( aco_fp2_total )
					,restr_fp1[i]
					,Utils.strDouble( aco_fp1_total )
					);
			result.add(insert);	
		}
			
			
		return result.iterator();
	}
	// this comment is useless
	public Iterator<String> getIteratorParadas()
	{
		double[][] disp = computeAvailabilityMatrix(false);
		//{indice_ld, indice_ob, indice_fp, indice_rh, indice_ml};
		
		ArrayList<String> list = new ArrayList<String>();
		for(int i=0; i<numDias; i++)
			list.add( String.format("%s,%s,%s,%s,%s,%s,%s,%s\n", 
					Utils.strDouble(disp[i][0]), 
					Utils.strDouble(disp[i][1]), 
					Utils.strDouble(disp[i][2]), 
					Utils.strDouble(disp[i][3]),
					Utils.strDouble(disp[i][4]), 
					Utils.strDouble(disp[i][5]), 
					Utils.strDouble(disp[i][6]), 
					Utils.strDouble(disp[i][7]) ) );
		
		return list.iterator();
	}
}
