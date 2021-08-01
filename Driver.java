/**
 * Name:           Paul Kim
 * PID:            A16464120
 * USER:           cs12sp21bce
 * File name:      Driver.java
 * Description:    Runs and Tests our HashTable class and also implements the
 *                 UCSDStudent class.
 */
import java.io.*;

/**
 * Class:            UCSDStudent
 * Description:      Class that extends the base. We will use this UCSDStudent
 *                   object to insert into the HashTable.
 *
 * Fields:           name - holds the data stored in the current node
 *                   studentNum - student
 *                   tracker - to track memory
 *
 * Public functions: UCSDStudent - constructor for UCSDStudent class
 *                   jettison - jettisons the object
 *                   equals - compares whether two objects are the same.
 *                   getName - retrieves the name of the UCSDStudent
 *                   hashCode - returns hashCode of the UCSDStudent
 *                   isLessThan - Checks if base object is less than the other
 *                   ToString - converts studentNum and name to string
 */
class UCSDStudent extends Base {
	private String name; //name of UCSD student
	private long studentNum; //student number
	private Tracker tracker; //to track memory

  /**
	 * Contructs the UCSDStudent class
	 *
	 * @param   nm       name of UCSD student
   * @param   sn       student number of UCSD student
	 * @return  None. initalizes all nember variables.
	 */
	public UCSDStudent (String nm, long sn) {

		name = nm;
    studentNum = sn;

		// DO NOT CHANGE THIS PART
		tracker = new Tracker ("UCSDStudent",
				Size.of (name)
				+ Size.of (studentNum)
				+ Size.of (tracker),
				" calling UCSDStudent Ctor");

	}

  /**
	 * jettisons object by jettisoning tracker
	 *
   * @param   None.
	 * @return  None. Jettisons tracker.
	 */
	public void jettison () {
		tracker.jettison();
    tracker = null;
	}

  /**
	 * checks if one object is equal to the other
	 *
	 * @param   object object to be compared.
	 * @return  True if the two objects are equal to each other
	 */
	public boolean equals (Object object) {
		if (this == object){
      return true;
    }

    if (!(object instanceof UCSDStudent)){
      return false;
    }

    UCSDStudent otherStu = (UCSDStudent) object;

    return name.equals(otherStu.getName());
	}

  /**
	 * gets the name variable of UCSDStudent
	 *
	 * @param   None.
	 * @return  name member variable.
	 */
	public String getName () {
		return name;
	}

  /**
	 * Calculates the hashcode and returns it.
	 *
	 * @param   None.
	 * @return  HashCode of the UCSDStudent class
	 */
	public int hashCode () {
    //hashCode to be returned
    int retval = 0;
    //index of each character in name
    int index = 0;

    //adds all the ascii codes together
    while (index != name.length ()) {
      retval += name.charAt (index);
      index ++;
    }

    return retval;
	}

  /**
	 * Checks if Base object is less than the other object
	 *
	 * @param   bbb     base object to be compared
	 * @return  true if parameter base is less than the current base object.
	 */
	public boolean isLessThan (Base bbb) {
		return (name.compareTo (bbb.getName ()) < 0) ? true : false;
	}

  /**
	 * Converts name and studentNum to string
	 *
	 * @param   None.
	 * @return  name and studentNum in the form of a String.
	 */
	public String toString () {
		return "name: " + name + " with StudentNum: " + studentNum;
	}
}

public class Driver {
	private static final int EOF = -1;
	private static final int HASH_TABLE_SIZE = 5;

	public static void main (String [] args) {

		/* initialize debug states */
		HashTable.debugOff();

		/* check command line options */
		for (int index = 0; index < args.length; ++index) {
			if (args[index].equals("-x"))
				HashTable.debugOn();
		}

		/* The real start of the code */
		SymTab symtab = new SymTab (HASH_TABLE_SIZE, "Driver");
		String buffer = null;
		int command;
		long number = 0;

		System.out.print ("Initial Symbol Table:\n" + symtab);

		while (true) {
			command = 0;    // reset command each time in loop
			System.out.print ("Please enter a command:\n"
					 + "(c)heck memory, "
					 + "(i)nsert, (l)ookup, "
					 + "(o)ccupancy, (w)rite:  ");

			command = MyLib.getchar ();
			if (command == EOF)
				break;
			MyLib.clrbuf ((char) command); // get rid of return

			switch (command) {

			case 'c':	// check memory leaks
				Tracker.checkMemoryLeaks ();
				System.out.println ();
				break;

			case 'i':
				System.out.print (
				"Please enter UCSD Student name to insert:  ");
				buffer = MyLib.getline ();// formatted input

				System.out.print (
					"Please enter UCSD Student number:  ");

				number = MyLib.decin ();

				// remove extra char if there is any
				MyLib.clrbuf ((char) command);

				// create Student and place in symbol table
				if(!symtab.insert (
					new UCSDStudent (buffer, number))) {

					System.out.println ("Couldn't insert "
							    + "student!!!");
				}
				break;

			case 'l':
				Base found;     // whether found or not

				System.out.print (
				"Please enter UCSD Student name to lookup:  ");

				buffer = MyLib.getline ();// formatted input

				UCSDStudent stu = new UCSDStudent (buffer, 0);
				found = symtab.lookup (stu);

				if (found != null) {
					System.out.println ("Student found!!!");
					System.out.println (found);
				}
				else
					System.out.println ("Student "
						+ buffer
						+ " not there!");

				stu.jettison ();
				break;

			case 'o':	// occupancy
				System.out.println ("The occupancy of"
						    + " the hash table is "
						    + symtab.getOccupancy ());
				break;

			case 'w':
				System.out.print (
				"The Symbol Table contains:\n" + symtab);
			}
		}

		/* DON'T CHANGE THE CODE BELOW THIS LINE */
		System.out.print ("\nFinal Symbol Table:\n" + symtab);

		symtab.jettison ();
		Tracker.checkMemoryLeaks ();
	}
}
