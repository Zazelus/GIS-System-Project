# GIS-System-Project
Written completely in Java, this GIS-System parses input from a text file as a 'database' and allows users to create 
scripts that support searching for specific locations based on a variety of parameters.

# Invocation
Make sure you have the latest java runtime environment installed.

Run using: **java GIS <database_file_name.txt> <script_file_name.txt> <desired_log_file_name.txt>**

The database and script files must exist. The third parameter will be the name of the log file that the program creates.

# Using a Text File for Input
As shown in the main directory, there are five database text files: VA_ALL.txt, VA_BATH.txt, VA_*.txt etc. These are
examples of text files that the program will take for input. If you are creating a new database from scratch, be
sure to follow the formatting of these files.

# Creating a Custom Script
Users can create their own scripts to run database lookups based on certain criteria. An example of such a script is
shown below, be sure to follow the same formatting:

# Script01.txt
```
; Script 1
;
; Very basic testing of the simple search commands with a tiny DB.
;
; Specify boundaries of coordinate space:
world	0794130W	0792630W	381500N	383000N
;
; Import some data:
import	VA_Monterey.txt
;
; Check the location and name/state indices:
show	quad
show	hash
;
; Let's try a single-match name/state search:
what_is	Trimble Knob	VA
;
; Let's check the buffer pool:
show	pool
;
; Let's try a single-match location search:
what_is_at	382812N	0793156W
;
; Let's check the buffer pool:
show	pool
;
; Let's try a few more name/state searches:
what_is	Swope Hollow	VA
what_is	Possum Trot	VA
what_is	Blue Grass	VA
;
; Let's check the buffer pool:
show	pool
;
; Let's try a few more location searches:
what_is_at	382145N	0793031W
what_is_at	382442N	0793451W
what_is_at	382607N	0793312W
;
; Let's check the buffer pool:
show	pool
;
; Let's try a two-match location search:
what_is_at	382856N	0793031W
;
; Let's check the buffer pool:
show	pool
;
; Exit
quit	
```
The commands that you can run are as follows:

| Command | Result |
| :---: | :---: |
| import      <database_text_file_name>: | Imports the database text file and stores features as hash table entries. | 
| show        <data_structure>:   | Shows the specified data structure and its contents. |
| show        <data_structure>:  | Shows the specified data structure and its contents. |
| what_is     <feature_name>: | Returns all features with this name. |
| what_is_at  <latitude_value> <longitude_value>: | Returns a feature at this coordinate. |
| what_is_in  <latitude_value> <longitude_value> <x_coord> <y_coord>: | Returns all features in this area. |

# After Running the Program
After parsing through the user's script file and running its commands, a log file will be created in the directory
specified in its command line parameter (should usually just be the current directory).

A typical log file looks like this (Script01.txt's specific log file actually):

```
Processing script file: Script01.txt

world	0794130W	0792630W	381500N	383000N

GIS Program

dbFile:     myDb01.txt
script:     Script01.txt
log:        myLog01.txt
Start time: Tue Apr 19 15:04:01 EDT 2022
Quadtree children are printed in the order SW  SE  NE  NW
--------------------------------------------------------------------------------

Latitude/longitude values in index entries are shown as signed integers, in total seconds.

World boundaries are set to:
			138600
	-286890		-285990
			137700
--------------------------------------------------------------------------------
Command 1: import	VA_Monterey.txt

Imported Features by name: 63
Imported Locations:	   62
Average name length:	   14
--------------------------------------------------------------------------------
Command 2: show	quad

------*
---------*
---------*
------------@
---------*
------------------------(-286627, 137860)
------------*
------------------@
------------*
------------------------(-286628, 137896)
------@
---------*
------------*
------------------------(-286479, 137945)
------------------@
------------*
------------------------(-286502, 138023)
------------@
------------------(-286523, 138047)
------------------(-286562, 138080)
------*
------------------------(-286433, 137756)
------------*
------------------@
------------------*
------------------*
------------------------------@
---------------------*
------------------------*
---------------------------*
------------------------------------------------------(-286356, 137772)
------------------------------------------------@
---------------------------*
------------------------------------------------------(-286358, 137773)
------------------------------------------@
------------------------*
------------------------*
------------------------------------@
---------------------*
---------------------*
------------------------------------(-286376, 137772)
---------------*
------------------------@
---------------*
---------------*
------------*
------------------------(-286314, 137725)
------------------------(-286224, 137708)
------------------@
------------*
------------*
------------@
------------------------------(-286304, 137827)
---------------*
------------------------@
---------------*
------------------------------(-286311, 137845)
------------*
------------------@
---------------*
------------------*
------------------*
------------------------------@
------------------------------------------(-286229, 137889)
---------------------*
------------------------------------@
------------------------------------------(-286219, 137891)
---------------------*
------------------*
------------------------@
---------------*
------------------------------(-286253, 137904)
------------*
---------*
------------------------------(-286212, 137722)
---------------*
------------------------@
---------------*
------------------------------(-286205, 137731)
------------*
------------------@
------------*
------------*
---------*
------------@
---------*
---------*
------@
------*
------------------------(-286438, 137942)
------------*
------------------@
------------------------(-286374, 138012)
------------*
---------*
------------@
------------*
------------*
------------------@
------------------------------(-286269, 138108)
------------------------------(-286231, 138105)
------------------------@
---------------*
---------------*
------------*
---------*
@
---------*
------------------(-286273, 138193)
------------@
---------*
------------------------(-286416, 138293)
---------------*
------------------------------(-286337, 138287)
------------------------@
------------------------------(-286345, 138312)
---------------*
------------------@
------------------------(-286353, 138352)
---------------*
---------------*
------------------------@
------------------*
------------------*
------------------------------@
------------------------------------------(-286392, 138367)
---------------------*
------------------------------------@
---------------------*
------------------------------------------(-286393, 138370)
------------------*
---------------*
------------(-286213, 138302)
------@
------*
------------------(-286329, 138414)
---------------*
---------------*
------------------------@
---------------*
------------------------------------(-286324, 138413)
------------------*
------------------------------@
------------------------------------(-286301, 138424)
------------------*
------------*
------------------@
------------------------(-286269, 138486)
------------------------------------------(-286321, 138438)
---------------------*
------------------------------------@
------------------------------------------(-286319, 138445)
---------------------*
------------------*
------------------------------@
------------------*
------------------*
---------------*
------------------------@
---------------*
---------------*
------------@
------------------------------(-286316, 138492)
---------------*
------------------------@
------------------------------(-286299, 138530)
---------------*
------------------------(-286231, 138536)
------------------@
------------------------(-286263, 138580)
------------------------(-286313, 138576)
------------*
------------*
------------------@
---------------*
---------------*
------------------------@
------------------------------(-286342, 138593)
------------------*
------------------*
------------------------------@
------------------------------------(-286358, 138591)
------------------------------------(-286383, 138597)
---------------*
---------------*
------------------------@
------------------*
------------------*
------------------------------@
------------------------------------(-286390, 138593)
------------------------------------(-286403, 138593)
------------------------------(-286428, 138574)
------*
------------------(-286561, 138234)
------------------(-286517, 138257)
------------@
------------*
---------------------*
------------------------*
------------------------------------------------(-286484, 138266)
------------------------------------------@
------------------------*
------------------------------------------------(-286486, 138267)
------------------------------------@
---------------------*
---------------------*
------------------*
------------------------------@
------------------*
------------------------------------------(-286491, 138282)
------------------------------------------(-286486, 138282)
------------------------------------@
---------------------*
------------------------------------------(-286490, 138284)
---------------*
------------------------@
------------------------------(-286449, 138309)
---------------*
------------------@
------------*
------------------------(-286500, 138341)
------------*
------------------------(-286582, 138315)
------------------@
------------*
------------------------(-286638, 138326)
------@
---------*
---------*
------------@
------------------(-286475, 138508)
------------------(-286612, 138532)
------*

--------------------------------------------------------------------------------
Command 3: show	hash

Number of elements: 63
Number of slots: 256
Maximum elements in a slot: 2
Load limit: 0.7

Slot Contents
    2: [[Buck Hill|VA, [675]], [Possum Trot|VA, [6940]]]
    7: [[New Salem Church|VA, [4128]]]
   10: [[Central Church|VA, [1078]]]
   11: [[Crab Run|VA, [1361]], [Hightown Church|VA, [2754]], [Bear Mountain|VA, [6434]]]
   12: [[Seybert Hills|VA, [7188]]]
   15: [[Seybert Chapel|VA, [4696]]]
   19: [[Gulf Mountain|VA, [2223]], [Barren Rock|VA, [4409]]]
   20: [[Union Chapel|VA, [5548]]]
   22: [[Strait Creek School (historical)|VA, [8513]]]
   28: [[Miracle Ridge|VA, [3879]]]
   30: [[Vance Hollow|VA, [5669]]]
   31: [[Claylick Hollow|VA, [1201]]]
   32: [[Blue Grass|VA, [387]]]
   43: [[Frank Run|VA, [1946]], [Trimble|VA, [7435]]]
   46: [[Peck Run|VA, [4256]]]
   53: [[Swope Hollow|VA, [5259]]]
   60: [[Thorny Bottom Church|VA, [5419]]]
   62: [[West Strait Creek|VA, [5829]], [Clover Creek Presbyterian Church|VA, [8081]], [Town of Monterey|VA, [8792]]]
   64: [[Meadow Draft|VA, [3720]]]
   69: [[Blue Grass School (historical)|VA, [7942]]]
   83: [[Sounding Knob|VA, [7309]]]
   90: [[Key Run|VA, [3162]]]
  105: [[Hamilton Chapel|VA, [2346]]]
  107: [[Trimble Knob|VA, [7562]]]
  115: [[Asbury Church|VA, [265]], [Wooden Run|VA, [6142]]]
  127: [[Strait Creek|VA, [5102]]]
  130: [[Burners Run|VA, [797]]]
  131: [[Monterey|VA, [7682]]]
  138: [[Davis Run|VA, [1513]]]
  141: [[Monterey District|VA, [8654]]]
  147: [[Doe Hill|VA, [6693]]]
  153: [[Smith Field|VA, [7818]]]
  154: [[Ginseng Mountain|VA, [2100]]]
  155: [[Monterey Methodist Episcopal Church|VA, [8369]]]
  163: [[Clover Creek|VA, [6560]]]
  175: [[Simmons Run|VA, [4819]]]
  184: [[Seldom Seen Hollow|VA, [4530]]]
  194: [[Hupman Valley|VA, [2877]]]
  197: [[Elk Run|VA, [1670]], [Little Doe Hill|VA, [3593]]]
  198: [[Lantz Mountain|VA, [3314]]]
  208: [[White Run|VA, [5991]]]
  211: [[Hannah Field Airport|VA, [6294]]]
  214: [[Laurel Run|VA, [3438]]]
  216: [[Rich Hills|VA, [7069]]]
  226: [[Forks of Waters|VA, [1822]]]
  233: [[Highland Elementary School|VA, [8225]]]
  241: [[New Hampden|VA, [6811]]]
  242: [[Jack Mountain|VA, [3037]]]
  243: [[Bluegrass Valley|VA, [515]]]
  247: [[Mount Carlyle|VA, [953]]]
  249: [[Highland High School|VA, [2473]]]
  250: [[Monterey Mountain|VA, [4001]]]
  251: [[Southall Chapel|VA, [4975]]]
  255: [[Highland Wildlife Management Area|VA, [2612]]]

--------------------------------------------------------------------------------
Command 4: what_is	Trimble Knob	VA

	7562:	Highland	(79d 35m 17s West, 38d 24m 17s North)
--------------------------------------------------------------------------------
Command 5: show	pool

MRU
	7562 1496326|Trimble Knob|Summit|VA|51|Highland|091|382417N|0793517W|38.404843|-79.5881037|||||952|3123|Monterey|09/28/1979|
LRU
--------------------------------------------------------------------------------
Command 6: what_is_at	382812N	0793156W

	The following features were found at (79d 31m 56s West, 38d 28m 12s North):
	 6940: Possum Trot Highland VA
--------------------------------------------------------------------------------
Command 7: show	pool

MRU
	6940 1496110|Possum Trot|Populated Place|VA|51|Highland|091|382812N|0793156W|38.4701196|-79.5322693|||||768|2520|Monterey|09/28/1979|
	7562 1496326|Trimble Knob|Summit|VA|51|Highland|091|382417N|0793517W|38.404843|-79.5881037|||||952|3123|Monterey|09/28/1979|
LRU
--------------------------------------------------------------------------------
Command 8: what_is	Swope Hollow	VA

	5259:	Highland	(79d 30m 5s West, 38d 15m 31s North)
--------------------------------------------------------------------------------
Command 9: what_is	Possum Trot	VA

	6940:	Highland	(79d 31m 56s West, 38d 28m 12s North)
--------------------------------------------------------------------------------
Command 10: what_is	Blue Grass	VA

	387:	Highland	(79d 32m 59s West, 38d 30m 0s North)
--------------------------------------------------------------------------------
Command 11: show	pool

MRU
	387 1481852|Blue Grass|Populated Place|VA|51|Highland|091|383000N|0793259W|38.5001188|-79.5497702|||||777|2549|Monterey|09/28/1979|
	6940 1496110|Possum Trot|Populated Place|VA|51|Highland|091|382812N|0793156W|38.4701196|-79.5322693|||||768|2520|Monterey|09/28/1979|
	5259 1487758|Swope Hollow|Valley|VA|51|Highland|091|381531N|0793005W|38.2587359|-79.5014329|381619N|0793014W|38.2719444|-79.5038889|541|1775|Monterey SE|09/28/1979|
	7562 1496326|Trimble Knob|Summit|VA|51|Highland|091|382417N|0793517W|38.404843|-79.5881037|||||952|3123|Monterey|09/28/1979|
LRU
--------------------------------------------------------------------------------
Command 12: what_is_at	382145N	0793031W

	The following features were found at (79d 30m 31s West, 38d 21m 45s North):
	 4530: Seldom Seen Hollow Highland VA
--------------------------------------------------------------------------------
Command 13: what_is_at	382442N	0793451W

	The following features were found at (79d 34m 51s West, 38d 24m 42s North):
	 8792: Town of Monterey Highland VA
--------------------------------------------------------------------------------
Command 14: what_is_at	382607N	0793312W

	The following features were found at (79d 33m 12s West, 38d 26m 7s North):
	 265: Asbury Church Highland VA
--------------------------------------------------------------------------------
Command 15: show	pool

MRU
	265 1481345|Asbury Church|Church|VA|51|Highland|091|382607N|0793312W|38.4353981|-79.5533807|||||818|2684|Monterey|09/28/1979|
	8792 2391311|Town of Monterey|Civil|VA|51|Highland|091|382442N|0793451W|38.4115829|-79.580854|||||884|2900|Monterey|02/19/2008|
	4530 1486995|Seldom Seen Hollow|Valley|VA|51|Highland|091|382145N|0793031W|38.3626223|-79.5086563|382227N|0793004W|38.3741667|-79.5011111|750|2461|Monterey SE|09/28/1979|
	387 1481852|Blue Grass|Populated Place|VA|51|Highland|091|383000N|0793259W|38.5001188|-79.5497702|||||777|2549|Monterey|09/28/1979|
	6940 1496110|Possum Trot|Populated Place|VA|51|Highland|091|382812N|0793156W|38.4701196|-79.5322693|||||768|2520|Monterey|09/28/1979|
	5259 1487758|Swope Hollow|Valley|VA|51|Highland|091|381531N|0793005W|38.2587359|-79.5014329|381619N|0793014W|38.2719444|-79.5038889|541|1775|Monterey SE|09/28/1979|
	7562 1496326|Trimble Knob|Summit|VA|51|Highland|091|382417N|0793517W|38.404843|-79.5881037|||||952|3123|Monterey|09/28/1979|
LRU
--------------------------------------------------------------------------------
Command 16: what_is_at	382856N	0793031W

	The following features were found at (79d 30m 31s West, 38d 28m 56s North):
	 1822: Forks of Waters Highland VA	 5102: Strait Creek Highland VA
--------------------------------------------------------------------------------
Command 17: show	pool

MRU
	5102 1487661|Strait Creek|Stream|VA|51|Highland|091|382856N|0793031W|38.4823417|-79.5086575|382442N|0793222W|38.4116667|-79.5394444|705|2313|Monterey|09/28/1979|
	1822 1483492|Forks of Waters|Locale|VA|51|Highland|091|382856N|0793031W|38.4823417|-79.5086575|||||705|2313|Monterey|09/28/1979|
	265 1481345|Asbury Church|Church|VA|51|Highland|091|382607N|0793312W|38.4353981|-79.5533807|||||818|2684|Monterey|09/28/1979|
	8792 2391311|Town of Monterey|Civil|VA|51|Highland|091|382442N|0793451W|38.4115829|-79.580854|||||884|2900|Monterey|02/19/2008|
	4530 1486995|Seldom Seen Hollow|Valley|VA|51|Highland|091|382145N|0793031W|38.3626223|-79.5086563|382227N|0793004W|38.3741667|-79.5011111|750|2461|Monterey SE|09/28/1979|
	387 1481852|Blue Grass|Populated Place|VA|51|Highland|091|383000N|0793259W|38.5001188|-79.5497702|||||777|2549|Monterey|09/28/1979|
	6940 1496110|Possum Trot|Populated Place|VA|51|Highland|091|382812N|0793156W|38.4701196|-79.5322693|||||768|2520|Monterey|09/28/1979|
	5259 1487758|Swope Hollow|Valley|VA|51|Highland|091|381531N|0793005W|38.2587359|-79.5014329|381619N|0793014W|38.2719444|-79.5038889|541|1775|Monterey SE|09/28/1979|
	7562 1496326|Trimble Knob|Summit|VA|51|Highland|091|382417N|0793517W|38.404843|-79.5881037|||||952|3123|Monterey|09/28/1979|
LRU
--------------------------------------------------------------------------------
Command 18: quit	

Terminating execution of commands.
End time: Tue Apr 19 15:04:01 EDT 2022
--------------------------------------------------------------------------------
```
