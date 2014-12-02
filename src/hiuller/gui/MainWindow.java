package hiuller.gui;

import hiuller.db.BancoDeDados;
import hiuller.gui.components.Console;
import hiuller.gui.components.Menu;
import hiuller.gui.dialogs.EditorParametros;
import hiuller.gui.dialogs.InserirDemanda;
import hiuller.gui.dialogs.InserirParada;
import hiuller.gui.dialogs.ListaParadas;
import hiuller.gui.dialogs.MonthPicker;
import hiuller.objectmodel.Plano;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.UIManager;

import com.itextpdf.awt.PdfGraphics2D;
import com.itextpdf.text.Document;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfTemplate;
import com.itextpdf.text.pdf.PdfWriter;

// the journey starts here
public class MainWindow extends JFrame
{
	private static final long serialVersionUID = 1L;
	public static final int VERSION = 1;

	public static void main(String[] args)
	{
		try {
			
			UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
			
			// http://docs.oracle.com/javase/tutorial/uiswing/lookandfeel/_nimbusDefaults.html
			UIManager.put("Table.showGrid", true);
			UIManager.put("TableHeader.cellBorder", BorderFactory.createLineBorder(Color.gray));
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		new MainWindow();
	}
	
	
	
	
	// it will be able to support more than one plan open at a time	
	private Plano plano;
	//
	private Menu menu;
	private EditorParametros editorParametros;	
	private Console console = new Console();
	
	private boolean autoexec = true;
		
	public MainWindow()
	{
		super(Constants.APP_TITLE);
		
		super.setIconImage( new ImageIcon( this.getClass().getClassLoader().getResource("factory.png") ).getImage() );
		
		this.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
		
		this.setSize( Constants.APP_STARTING_DIM );
		
		this.setLocationRelativeTo(null);
		
	// layout setup
		this.setLayout(new BorderLayout());

		editorParametros = new EditorParametros();
		
		menu = new Menu(this);
		this.add(menu, BorderLayout.NORTH);		
	// end of layout setup
		
		console.print("Iniciada operação.");
		
	// So that everyone can talk to the main window
		Constants.mainWindowHandle = this;
		
		this.setVisible(true);
		
// this flag helps to automatically create a new plan for testing purposes
// all automation can be done here.
		if(autoexec)
		{
			openFile( (new File(".")).getAbsolutePath() + "\\temp_data\\out3.pla");
			//calcular();			
			//testar();
			//showConsole();
			
			//exportPDF();
/*			
		// create a new plane
			Calendar c = Calendar.getInstance();
			c.set(Calendar.DAY_OF_MONTH, 1);			
			plano = new Plano( c.getTime() );
			this.add(plano, BorderLayout.CENTER);			
			menu.setOpen(true);			
			this.setVisible(true);
			
		// 
			Constants.setDate( c.getTime() );

		// load paradas from local XML file
			LeitoraParadasXML leitora = new LeitoraParadasXML((new File(".")).getAbsolutePath() + "\\temp_data\\agosto_2014.xml");
			plano.carregarParadas( leitora.parse() );
			
		// change some parameters
			editorParametros.setParameter(EditorParametros.PROD_AF1, 1800.0);
			editorParametros.setParameter(EditorParametros.PROD_AF2, 1800.0);
			editorParametros.setParameter(EditorParametros.PROD_AF3, 6800.0);
			
		// manually change some values
			int[] parametros = new int[]{
				16,16,16,14,15,16,15,15,16,16,16,14,15,16,14,15,16,14,14,14,14,16,16,16,16	
			};
			int[] dias = new int[]{
				1,2,3,6,6,6,7,8,13,14,15,18,18,18,19,19,19,20,21,22,23,27,28,29,30	
			};
			double[] vals = new double[]{
				0,4500,5700,1650,300,6600,800,1600,1000,3400,6300,1650,1650,6600,1650,1650,6600,
				300,0,1000,1500,1000,0,4500,5700
			};
			
			for(int i=0; i<parametros.length;i++)
				plano.inserirAlteracaoManual(new AlteracaoManual(parametros[i], dias[i], vals[i]));
			
		// simulate
			calcular();
*/			
		}
		
	}
	
	public void showEditorParametros()
	{
		editorParametros.showDialog();		
	}
	
	
	public void newPlano()
	{
		MonthPicker mp = new MonthPicker();
		if( mp.hasResult() )
		{
			String selecao = mp.getTimeString();
			Date date = null;
			try {
				 
				date = new SimpleDateFormat("dd/MM/yyyy").parse(selecao);
				
			} catch (Exception e)
			{
				// i think it will never come here
			}
			
			Constants.setDate(date);
			
			plano = new Plano(date);
			this.add(plano, BorderLayout.CENTER);
			
			menu.setOpen(true);
			
			this.setVisible(true);
		}
	}
	
	public void calcular()
	{
		Runnable runnable = new Runnable()
		{
			public void run()
			{
				try
				{
					evolve();
				} catch (Exception e)
				{
					e.printStackTrace();
				}
			}
		};
		
		Thread t = new Thread(runnable);
		t.start();
	}
	
	public void evolve() throws Exception
	{
/* comentado para implantação do PBR		
		// the optimization loop goes in here
		
		//showConsole();
		ProgressMonitor progressMonitor = new ProgressMonitor(null, "Evolving genetic pool...", "0%", 0, 100);
		
				int numDias = plano.getNumDias();
		
				FitnessFunction fitness = new PlanoFitness(plano);
				
				Configuration conf = new DefaultConfiguration();
				conf.setFitnessFunction(fitness);
							
				Gene[] gene = new Gene[numDias*4];		
				
				for(int i=0; i<numDias*4; i++)
				{
					int maxDaily = 0;
					
					if(i < numDias)
						maxDaily = Constants.MAX_SERIE_HIC; // max number of HIC, will multiply this by four
					else
						if(i < 2*numDias)
							maxDaily = Constants.MAX_CORR_DR;
						else
							if(i < 3*numDias)
								maxDaily = Constants.MAX_CORR_RH;
							else
								maxDaily = Constants.MAX_CORR_FP;
					
					gene[i] = new IntegerGene(conf, 0, maxDaily);
				}
				
				Chromosome sampleChromosome = new Chromosome(conf, gene);
				conf.setSampleChromosome(sampleChromosome);
				
				conf.setPopulationSize(Constants.POPULATION_SIZE);
				
				Genotype population = Genotype.randomInitialGenotype( conf );
				
				for(int i=0; i<Constants.MAX_NUMBER_EVOLUTIONS; i++)
				{
					
					if(progressMonitor.isCanceled())
					{
						break;
					}
					
					Constants.mainWindowHandle.print2Console("Iniciando Evolução");
					population.evolve();
					//JOptionPane.showMessageDialog(null, "Just evolved a little");
										
					IChromosome bestSoFar = population.getFittestChromosome();
					double fitnessValue = bestSoFar.getFitnessValue();

					int percentage = (int) (((double) i/(double)Constants.MAX_NUMBER_EVOLUTIONS)*100.0);
					progressMonitor.setNote(String.format("%d%% Fitness = %.1f", percentage, fitnessValue));
					progressMonitor.setProgress(percentage);		
					
					
					//plano.setSolutionProvider( ((PlanoFitness)fitness).getProvider(bestSoFar) );
					//plano.update();
				}
				
				IChromosome best = population.getFittestChromosome();
				((PlanoFitness) fitness).send(best);
*/				
		
		// TODO verificar entradas
		
		// TODO categorizacao dos dias. dados crus na saida 'availability.csv'.
		
	}
	
	public EditorParametros getParametros()
	{
		return editorParametros;
	}
	
	public void inserirParada()
	{
		new InserirParada( this.plano );
	}
	
	public void inserirDemanda()
	{
		plano.showInserirDemanda();
	}
	
	public void listarParadas()
	{
		if(plano == null)
			JOptionPane.showMessageDialog(null, "Plano is null!");
		else 
		{
			new ListaParadas(plano);
			plano.update();
		}
	}

	// save file to disk
	public void writeToFile(String location)
	{
		BancoDeDados temp = new BancoDeDados(location, true);
		temp.savePlano(plano);
	}

	public void openFile(String location)
	{
		BancoDeDados temp = new BancoDeDados(location, false);
		Plano plano = temp.readPlano();
		
		this.plano = plano;
		this.add(this.plano, BorderLayout.CENTER);
		
		menu.setOpen(true);
		
		console.print(String.format("Abriu arquivo <%s>.", location));
		
		this.setVisible(true);
	}

	public InserirDemanda getInserirDemanda()
	{
		return plano.getInserirDemanda();
	}

	public void showConsole()
	{
		console.showConsole();
	}
	public void print2Console(String str)
	{
		console.print(str);
	}

	public void testar()
	{		
		plano.runTest();
	}
	
	public void exportPDF()
	{	
		// FIXME create file chooser
		String fileLocation = (new File(".")).getAbsolutePath() + "\\temp_data\\teste.pdf";
		
		File file = new File(fileLocation);
		
		Document document = new Document(PageSize.A4.rotate());
		try {
			
			PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(file));
			document.open();
			
			PdfContentByte cb = writer.getDirectContent();
			
			float height = PageSize.A4.getHeight();
			float width  = PageSize.A4.getWidth();
						
			PdfTemplate tp = cb.createTemplate(width, height);
			Graphics2D g2 = new PdfGraphics2D(cb, width, width);			
			
			Dimension temp = plano.getScroll().getPreferredSize();
			
			plano.getScroll().setSize(temp);	
	        plano.getScroll().addNotify();
	        plano.getScroll().doLayout();
	        plano.getScroll().validate();
			
			//g2.scale(1.8, 1.8);
						
			plano.getScroll().paintAll(g2);
			//plano.printAll(g2);
			
			g2.dispose();
			cb.addTemplate(tp, 0, 0);
			
			writer.setPageEmpty(false);
			
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		document.close();
		
		JOptionPane.showMessageDialog(null, String.format("Foi criado PDF <%s>", fileLocation));
		
	}

	public void limpar()
	{		
		plano.limpar();
	}

	public void exportar()
	{
	// write the plano table to disk
		// FIXME create file chooser
		String dir = (new File(".")).getAbsolutePath();
		String filePath = dir + "\\temp_data\\plano.csv";		
		String header = Plano.FIELD_LIST + '\n';
		Iterator<String> values = plano.getCSViterator();		
		writeIterator2file(filePath, header, values);
	// write parameters to disk
		String paramPath = dir + "\\temp_data\\params.csv";		
		String paramHeader = "parametro,valor\n";
		Iterator<String> params = editorParametros.getCSVIterator();		
		writeIterator2file(paramPath, paramHeader, params);
	// write availability matrix
	//{indice_ld, indice_ob, indice_fp, indice_rh, indice_ml};
		String aPath = dir + "\\temp_data\\availability.csv";		
		String aHeader = "indice_ld, indice_ob, indice_fp, indice_rh, indice_ml, indice_ld1, indice_fp1, indice_ml4\n";
		Iterator<String> a = plano.getIteratorParadas();		
		writeIterator2file(aPath, aHeader, a);
	}

	private void writeIterator2file(String filePath, String header,
			Iterator<String> values)
	{
		File file = new File(filePath);
		
		if(file.exists())
			file.delete();
		
		
		Writer out = null;
		try
		{
			out = new FileWriter(file);
		} catch (IOException e)
		{
			e.printStackTrace();
		}
		BufferedWriter writer = new BufferedWriter(out);


		try {
			
			writer.write(header);
		
			while(values.hasNext())
			{
					writer.write(values.next());
			}
		} catch(IOException e) {
			e.printStackTrace();
		}
		
		try {
			writer.close();
			out.close();

		} catch(Exception e) {
			e.printStackTrace();
		}
	}

}
