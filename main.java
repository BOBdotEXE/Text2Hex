
package txt2hex;
import java.io.*;
import java.util.Scanner;
import javax.xml.bind.annotation.adapters.HexBinaryAdapter;

  /**
   * This is a simple program that will convert a users text into raw hex code.
   * It does this by first figuring out where the is that the user would like to process.
   * It can handle a simple string passed as an argument, or it will read from a text file.
   * Once it has the users data, it will pass various check to make sure it's valid hex.
   * The data is also simplified, converting it to all caps, and removing any spaces
   * After the data has been verified, it will be converted to 'byte code' then written to a file.
   *  
   * @author  BOBdotEXE
   * @version 0.4
   *
   **/

public class main {

	
	/**
	 * This is the main controller of the program, to keep it simple, 
	 * This will only perform basic checks, and variable hand-off
	 * The bulk of the work will be handled by dedicated functions.
	 * 
	 * @param  [input type] '-i' or '-f' to Read from user (I)nput, or (F)ile
	 * @param  [data] The file path of the data to be converted, or just a sting of data
	 * @param  [output file] Where the raw dats should be stored.
	 * 
	 **/
	public static void main(String[] args)
	{
		
		String userText=""; //the text that will hold the user's data.
		if (args.length !=3)
		{	
			System.err.println("Incorrect number of arguments!\n"+
		                        "Expected: 3 Got: "+args.length+"! \n"+
								"\nUsage:  Text2Hex.jar [input type] [data] [output file]");
			System.exit(-1);
		}
		
		//Save arguments to variables for easier code reading
		String inputType=args[0];
		String passedInput=args[1];
		String outPath=args[2];
		
		inputType=inputType.toLowerCase(); //just to make things easy, we'll force the input to lower case.
		
		if (!(inputType.equals("-i")) && !(inputType.equals("-f")) ) //is the first ARG one of the choices we offered?
		{
			//if not, show an error
				System.err.println("Incorrect argument (#1)\n"+
						"This can only be -i or -l");
				System.exit(-1);
		}
		 
		
		if ( inputType.equals("-f") ) //if user wants to read from a text file 
		{
			//First, make sure the file exists.
			File checkFile = new File( passedInput );
			if(!checkFile.exists() || checkFile.isDirectory())
			{ 
				//If the files does not exist, or it's just a folder.	
				//Give an error
				System.err.println("The requested file: '"+passedInput+"' could not be opened.");
				System.exit(-1);
			
			}
			
			//If the file does exist, go ahead and process it.
			try {
				userText=file2String(passedInput);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
					
		}
		
		if ( inputType.equals("-i") ) //if user has manually typed data 
		{
			//Double check to make sure they did not actually give a file instead...
			File checkFile = new File( passedInput );
			
			if(checkFile.exists() || checkFile.isDirectory() )
			{
				System.err.println("Incorrect arguments (#2)\n"
				                   +"File/Folder given, not hex data! ");
				System.exit(-1);
			}
			
			userText=passedInput; //Save the data the user gave	
		}
		 
		//Convergence!
		//Check the users data!
		
		if (isHexGood(userText) ==false)
		 {
			System.err.println("Invalid data!");
		 }
		
		//Last sanity check
		//convert user text to uppercase, and trim whitespace
		String userData = trimData(userText);
		
		//TODO Move file check to separate method
		
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
				Scanner reader = new Scanner(System.in); 
				String answer = reader.next(); //ask the user if they'd like to overwrite
				reader.close();
				if (answer.toUpperCase().indexOf('Y') <0 ) //if they Don't type 'Y' or 'y'
				{
					System.out.println("\nshutting down!");
					System.exit(-1);					 //end the program
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
	
	
	/**
	 *  Reads the text file requested by the user (with no line ends)
	 *  Saves the processed data as a string.
	 * 
	 * @param filePath - Path to the file we will read
	 * @return String - Processed data
	 * @throws IOException
	 */
	public static String file2String(String filePath) throws IOException
	{
		BufferedReader buf = new BufferedReader(new FileReader(filePath));	        
		String readData=""; //What we're reading at the moment
		String fullList=""; //all the data we have read so far
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


	/**
	 * Validates the users hex code (string)
	 * This is done by looking at each character in the string, and comparing it
	 * It to out list of valid hex data (and the space character) as that will be ignored later.
	 * If any invalid data is found the program will display an error with the invalid value, and it's location.
	 * Will return 'true' if the data is valid.
	 * 
	 * @param hexCode - String of data to check
	 * @return bool - is data valid?
	 */
	public static boolean isHexGood(String hexCode)
	{
	char lookingAt;
	String validHex=" 1234567890ABCDEF"; //All the valid hex chracters for comparison.
	
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


	/**
	 * This method reads though the users data, and removes any spaces,
	 * and converts the whole thing to upper case.
	 * With all the data being uniform it's much easier to convert later on.
	 * 
	 * 
	 * @TODO merge this with 'isHexGood' this thread is redundant.
	 * 
	 * @param hexCode - Sting of data to trim
	 * @return Trimmed, string
	 */
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


	/**
	 * This is the function that does the actual writing.
	 * It takes the (converted and verified) string of hex data
	 * And does one last check to make sure it contains an even amount of data
	 * (Hex code MUST be in pairs)
	 * If the code is not in pairs (odd amount of data) then it will give an error and halt.
	 * 
	 * If the data passes the final validity check, the output file will be opened,
	 * The user's data is converted into an array of bytes, 
	 * then that array of data is written to the file, one byte at a time
	 * 
	 *@param hex - The String of hex data to convert
	 * @param filePath - The path of the file to open
	 * @throws IOException
	 */
	public static void writeHex(String hex,String filePath) throws IOException
	{
		if( (hex.length()%2)!=0) //check if the length is even
		{
			//if it's odd.
			System.err.println("Input contains in invald amount of data, hex code must be even!");
			System.exit(-1);
			
		}
		OutputStream rawFile = new FileOutputStream(filePath);
		HexBinaryAdapter hexConverter = new HexBinaryAdapter();
		byte[] hexbite = hexConverter.unmarshal(hex);
		for (int i = 0; i <= hexbite.length-1;i++)
		{
			rawFile.write( hexbite[i] );
		}
		rawFile.close();
	
	    System.exit(0);
	}
}
