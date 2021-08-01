/**
 * Name:           Paul Kim
 * PID:            A16464120
 * USER:           cs12sp21bce
 * File name:      HashTable.java
 * Description:    This program creates an HashTable that we will use to enter
                  UCSDStudent objects into and interact with
 */

 /**
 * Class:            HashTable
 * Description:      Class that extends from base. It allows us to store Base
 *                   objects into a HashTable.
 *
 * Fields:           Counter - number of HashTables so far
 *                   debug - allocation of debug states
 *                   occupancy - how many elements are in the Hash Table
 *                   table - the Hash Table itself
 *                   tableCount - which hash table it is
 *                   tracker - to track memory
 *                   index - last location checked in hash table
 *                   count -count of location in probe sequence
 *
 * Public functions: HashTable - Constructor for HashTable class
 *                   debugOn            - Turn debug option on
 *                   debugOff           - turn debug option off
 *                   jettison             - display the current node.
 *                   getOccupancy       - returns occupancy
 *                   insert             - default call to insert
 *                   insert             - insert method with boolean parameter
 *                   locate             - locates index in table where the
 *                                        location is to be performed
 *                   lookup             - looks up the element in the hashtable
 *                   toString           - converts to string
 *
 */
public class HashTable extends Base {

	// counters, flags and constants
	private static int counter = 0;         // number of HashTables so far
	private static boolean debug;           // allocation of debug states

	// data fields
	private long occupancy;     // how many elements are in the Hash Table
	private int size;           // size of Hash Table
	private Base table[];       // the Hash Table itself ==> array of Base
	private int tableCount;     // which hash table it is
	private Tracker tracker;    // to track memory

	// initialized by Locate function
	private int index;      // last location checked in hash table

	// set in insert/lookup, count of location in probe sequence
  private int count = 0;

	// messages
	private static final String DEBUG_ALLOCATE = " - Allocated]\n";
	private static final String DEBUG_LOCATE = " - Locate]\n";
	private static final String DEBUG_LOOKUP = " - Lookup]\n";
	private static final String AND = " and ";
	private static final String BUMP = "[Bumping To Next Location...]\n";
	private static final String COMPARE = " - Comparing ";
	private static final String FULL = " is full...aborting...]\n";
	private static final String FOUND_SPOT = " - Found Empty Spot]\n";
	private static final String HASH = "[Hash Table ";
	private static final String HASH_VAL = "[Hash Value Is ";
	private static final String INSERT = " - Inserting ";
	private static final String PROCESSING = "[Processing ";
	private static final String TRYING = "[Trying Index ";


/*---------------------------------------------------------------------------
Function Name:                debugOn
Purpose:                      Turns on debugging for this HashTable
Description:                  Turns debug variable to true
Input:                        None.
Output:                       None.
Result:                       Debug becomes true.
Side Effects:                 None.
---------------------------------------------------------------------------*/
	public static void debugOn () {
		debug = true;
	}

/*---------------------------------------------------------------------------
Function Name:                debugoff
Purpose:                      Turns off debugging for this HashTable
Description:                  Turns debug variable to false.
Input:                        None.
Output:                       None.
Result:                       Debug becomes false.
Side Effects:                 None.
---------------------------------------------------------------------------*/
	public static void debugOff () {
		debug = false;
	}

/*---------------------------------------------------------------------------
Function Name:                HashTable
Purpose:                      Constructor for HashTable class
Description:                  Initializes all the member variables
Input:                        sz - size of HashTable
                              caller - used to initalize a new Tracker object
Output:                       None.
Result:                       Object is created.
Side Effects:                 None.
---------------------------------------------------------------------------*/
	public HashTable (int sz, String caller) {

		occupancy = 0;
    counter = counter + 1;
    tableCount = counter;
    size = sz;
    table = new Base[size];

		// DO NOT CHANGE THIS PART
		tracker = new Tracker ("HashTable",
				Size.of (index)
				+ Size.of (occupancy)
				+ Size.of (size)
				+ Size.of (table)
				+ Size.of (tableCount)
				+ Size.of (tracker),
				caller + " calling HashTable Ctor");

        //debug message
        if (debug){
          System.err.print(HASH + tableCount + DEBUG_ALLOCATE);
        }
	}

/*---------------------------------------------------------------------------
Function Name:                jettison
Purpose:                      jettisons HashTable
Description:                  Initializes all the member variables
Input:                        sz - size of HashTable
                              caller - used to initalize a new Tracker object
Output:                       None.
Result:                       Object is created.
Side Effects:                 None.
---------------------------------------------------------------------------*/
	public void jettison () {

		//jettison each individual index
		for(int element = 0; element < size; element++){
			if(table[element] != null){
        table[element].jettison();
      } 
		}

		tracker.jettison();
    tracker = null;

    //debug message
    if (debug){
      System.err.print (String.format (DEBUG_ALLOCATE,
                      tableCount));
    }
	}

/*---------------------------------------------------------------------------
Function Name:                getOccupancy
Purpose:                      Gets current occupancy of the Hash Table
Description:                  returns occupancy member variable
Input:                        None.
Output:                       returns occupancy variable
Result:                       occupancy is returned.
Side Effects:                 None.
---------------------------------------------------------------------------*/
	public long getOccupancy () {
		return this.occupancy;
	}

	/**
	 * Performs insertion into the table via delegation to the
	 * private insert method.
	 *
	 * @param   element       The element to insert.
	 * @return  true or false indicating success of insertion
	 */
	public boolean insert (Base element) {
		return insert (element, false);
	}

/*---------------------------------------------------------------------------
Function Name:                insert
Purpose:                      performs insertion into the table.
Description:                  returns occupancy member variable
Input:                        element - element we will insert
                              recursiveCall - Boolean for recursive call
Output:                       None.
Result:                       Element is inserted into HashTable accordingly.
Side Effects:                 None.
---------------------------------------------------------------------------*/
	public boolean insert (Base element, boolean recursiveCall) {
		//debug message
		if (debug){
			System.err.print(HASH + tableCount + INSERT + element.getName() + "]\n");
		}
    
    //reset index and count
		if(recursiveCall == false){
			index = -1;
			count = 0;
		}

		count++;
		//checks if recursive count exceeds the size
		if(count == size){
      //debug message
        if (debug){
          System.err.print(FULL);
        }
			return false;
		}
   

		//debug message
		if (debug){
			System.err.print(HASH + tableCount + DEBUG_LOCATE);
		}

		//holder for what locate returns
		Base holder = locate(element);

		//if the duplicate element is at corresponding index
		if(holder != null){
			table[index].jettison();
			table[index] = element;
			return true;
		}

		//if there is an empty spot at index (base case)
		if(holder == null){
			if(table[index] == null){
				table[index] = element;
				occupancy++;
				return true;
			}

			//else means the occupying element is less than current
			else {
        
        Base recursive = table[index];
        //hold occupying element for recursive call
				table[index].jettison();
				table[index] = element;

        //debug message
        if (debug){
          System.err.print(BUMP);
        }
				//recursive Call to bump element to next position
				insert(recursive, true);
			}
		}
		return true;
	}

/*---------------------------------------------------------------------------
Function Name:                locate
Purpose:                      Locate the index in the table where the insertion
                              is to be performed.
Description:                  Finds the hashvalue and increment to efficiently
                              locate where the element should go.
Input:                        element - element we will insert
                              recursiveCall - Boolean for recursive call
Output:                       None.
Result:                       Element is inserted into HashTable accordingly.
Side Effects:                 None.
---------------------------------------------------------------------------*/
	private Base locate (Base element) {

    //debug message
    if (debug){
      System.err.print(PROCESSING + element.getName() + "]\n");
    }
      
  	//save hash Value
  	int hashValue = element.hashCode();

		//debug message
		if (debug){
			System.err.print(HASH_VAL + hashValue + "]\n");
		}

  	//save increment of index
  	int increment = element.hashCode() % (size - 1) + 1;

		//for efficiency
  	if(index == -1){
  		//initial index
  		index = hashValue % size;
  	}
  	else{
  		index = (index + increment) % size;
  	}


  	for(int loopCount = 0; loopCount < size; loopCount++){
			//debug message
			if (debug){
				System.err.print(TRYING + index + "]\n");
			}

			//checks if index is empty
    	if(table[index] == null){

				//debug message
        if (debug){
          System.err.print(HASH + tableCount + FOUND_SPOT);
        }

				return null;
    	}

      //debug message
      if (debug){
        System.err.print(HASH + tableCount + COMPARE + element.getName() + 
                         AND + table[index].getName() + "]\n");
      }

    	//checks if same item at index is found
    	if(table[index].equals(element)){
				return table[index];
    	}

    	//checks if occupied element in index is less than current
    	//element
    	if(table[index].isLessThan(element)){
				return null;
    	}

    	index = (index + increment) % size;
    }
    return null;
	}

/*---------------------------------------------------------------------------
Function Name:                lookup
Purpose:                      Looks up the element in the hash table.
Description:                  Calls locate and returns whatever is returned
Input:                        element - element to look up in HashTable
Output:                       Returns reference to the element if found, null
                              otherwise.
Result:                       Index is changed.
Side Effects:                 None.
---------------------------------------------------------------------------*/
	public Base lookup (Base element) {
    index = -1;
    
    //debug message
		if (debug){
			System.err.print(HASH + tableCount + DEBUG_LOOKUP);
		}
   
   //debug message
		if (debug){
			System.err.print(HASH + tableCount + DEBUG_LOCATE);
		}
    
		//variable to hold and compare whats returned
    Base holder = locate(element);

		//only returns holder if locate returns a non null element.
    if(holder != null){
      return holder;
    }
    return null;
	}


	/**
	 * Creates a string representation of the hash table. The method
	 * traverses the entire table, adding elements one by one ordered
	 * according to their index in the table.
	 *
	 * @return  String representation of hash table
	 */
	public String toString () {
		String string = "Hash Table " + tableCount + ":\n";
		string += "size is " + size + " elements, ";
		string += "occupancy is " + occupancy + " elements.\n";

		/* go through all table elements */
		for (int index = 0; index < size; index++) {

			if (table[index] != null) {
				string += "at index " + index + ": ";
				string += "" + table[index];
				string += "\n";
			}
		}

		string += "\n";

		if(debug)
			System.err.println(tracker);

		return string;
	}
}
