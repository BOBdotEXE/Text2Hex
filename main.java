package txt2hex;
import java.io.*;
import java.util.Scanner;
import javax.xml.bind.annotation.adapters.HexBinaryAdapter;
public class main {

	public static void main(String[] args)
	{
		
		String userText=""; //the text that will hold the user's data.
		if (args.length !=3)
		{	
			System.err.println("Incorrect number of arguments!\n"+
		                        "Expected: 3 Got: "+args.length+" !");			
			System.exit(-1);
		}
		
		String outPath=args[2];
		
		args[0]=args[0].toLowerCase(); //just to make things easy, we'll force the input to lower case.
		
		if (!(args[0].equals("-i")) && !(args[0].equals("-f")) ) //is the first ARG one of the choices we offered?
		{
			//if not, show an error
				System.err.println("Incorrect arguments (#1)");
				System.exit(-1);
		}
		 
		
		if ( args[0].equals("-f") ) //if user wants to read from a text file 
		{
			//First, make sure the file exists.
			File checkFile = new File( args[1] );
			if(!checkFile.exists() || checkFile.isDirectory())
			{ 
				//If the files does not exist, or it's just a folder.	
				//Give an error
				System.err.println("The requested file: '"+args[1]+"' could not be opened.");
				System.exit(-1);
			
			}
			
			//If the file does exist, go ahead and process it.
			try {
				userText=file2String(args[1]);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			

			
		}
		
		if ( args[0].equals("-i") ) //if user has manually typed data 
		{
			//Double check to make sure they did not actually give a file instead...
			File checkFile = new File( args[1] );
			
			if(checkFile.exists() || checkFile.isDirectory() )
			{
				System.err.println("Incorrect arguments (#2)\n"
				                   +"File give, not hex data! ");
				System.exit(-1);
			}
			
			userText=args[1]; //Save the data the user gave	
		}
		 
		//Convergance!
		//Check the users data!
		
		if (isHexGood(userText) ==false)
		 {
			System.err.println("Invalid data!");
		 }
		
		//Last sanity check
		//convert user text to uppercase, and trim whitespace
		String userData = trimData(userText);
		
		
		//get ready to write to the file
		File checkFile = new File( outPath );
			if( checkFile.isDirectory())
			{ 
				//If the files does not exist, or it's just a folder.	
				//Give an error
				System.err.println("Was give a folder to write to, please specify a file");
				System.exit(-1);
			}
			if(checkFile.exists())
			{
				System.out.print("\nFile already exists! \n Overwright? (y/n):");
				Scanner reader = new Scanner(System.in);  // Reading from System.in
				String answer = reader.next();

				if (answer.toUpperCase().indexOf('Y') <0 )
				{
					System.out.println("\nshutting down!");
					System.exit(-1);
				}
			}
			else //create new file
			{
				  new File(outPath);
			}
			
		try {
			writeHex(userData,outPath);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public static void writeHex(String hex,String FilePath) throws IOException
	{
		if( (hex.length()%2)!=0) //check if the length is even
		{
			//if it's odd.
			System.err.println("input contains in invald amount of data, hex code must be even");
			System.exit(-1);
			
		}
		OutputStream os = new FileOutputStream(FilePath);
		HexBinaryAdapter adapter = new HexBinaryAdapter();
	/*	
		for (int i = 0; i <= hex.length()-1; i=i+2) //read two digits at a time
		{
			String hexBite;
			hexBite=hex.substring(i,i+2);
			System.out.println(hexBite);
		    byte hexCode = Byte.parseByte(hexBite);
		    os.write( hexCode );
		}
		
		*/
		byte[] hexbite = adapter.unmarshal(hex);
	    //byte hexCode = Byte.parseByte(hex);
		for (int i = 0; i <= hexbite.length-1;i++)
		{
			os.write( hexbite[i] );
		}
	    os.close();

	    System.exit(0);
	}
	 
	public static String file2String(String filePath) throws IOException
	{
		BufferedReader buf = new BufferedReader(new FileReader(filePath));	        
		String readData="";
		String fullList="";
		 try {
			while((readData= buf.readLine()) != null){
				fullList=fullList+readData;
				 }
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		buf.close();
		
		return fullList;
	}


	public static boolean isHexGood(String hexCode)
	{
	char lookingAt;
	String validHex=" 1234567890ABCDEF";
	  for (int i = 0; i <= hexCode.length()-1; i++) { //look though each letter in the users text
		  lookingAt=hexCode.toUpperCase().charAt(i);            //look though each letter in the users text (uppercase, just makes it easier
		  if (validHex.indexOf(lookingAt) == -1) //make sure whatever you're reading is valid
		  {
			//Looks like we found invalid data...
			System.err.println("Invalid data '"+lookingAt+"' Found at Pos: "+(i+1)+" !");
			System.exit(-1);			
			}
	  	}
	  return true;
	 }
	  
 
	public static String trimData(String hexCode)
	{
	char lookingAt;
	String validHex=" 1234567890ABCDEF";
	String newData="";
	  for (int i = 0; i <= hexCode.length()-1; i++) { //look though each letter in the users text
		  lookingAt=hexCode.toUpperCase().charAt(i);            //look though each letter in the users text (uppercase, just makes it easier
		  if (validHex.indexOf(lookingAt) == -1) //make sure whatever you're reading is valid
		  {
			//Looks like we found invalid data...
			System.err.println("Invalid data '"+lookingAt+"' Found at Pos: "+(i+1)+" !");
			System.exit(-1);			
			}
		  else // add the character to our new string
		  {
			if (validHex.indexOf(lookingAt) !=0) //assuming it's NOT a space (the first character in our hex chart)
			{
				newData=newData+lookingAt;
			}
		  }
	  	}
	  return newData;
	 }
}
