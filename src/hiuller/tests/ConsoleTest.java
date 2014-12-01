package hiuller.tests;

import hiuller.gui.components.Console;

public class ConsoleTest
{

	public static void main(String[] args)
	{
		Console console = new Console();
		console.showConsole();

		
		console.print("Primeira");
		console.print("Segunda");
		console.print("Terceira Linha");
		
	}

}
