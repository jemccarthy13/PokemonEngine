This file will be replaced with a more in-depth documentation at some point in the future.

For now this will be the location of any @todo action items, desired features, and committed updates.  

Also documented will be known bugs.
===================================================================
Version 3.0.0                                           21 Sep 2015
===================================================================
Features:
- Update to MVC design pattern
- Flyweight object construction
- Graphics enhancements (added icons)
- Battle engine enhancements
  -- Wild encounters added
  -- Switching pokemon added
  -- Level up tested in battle
- Abstracted out many "Pokemon" specific terminology / references
- Added sample automated test
- Partial convert to Javadoc style comments instead of
  custom commenting
 
 todos:
- Need to enable more options (create better options scenes to paint)
- Create sprites for more NPCs
- Pokegear abstraction / implement sub-menus
- Need to finish abstraction
- Reduce McCabe Cyclomatic Complexity - interfaces / objects with
  appropriate methods
- Make editors for locations / pokemon / NPCs to allow easier
  editing of the raw data
- Analyze plug and play components for simplicity and potential
  for redesign
===================================================================
Version 2.0.0                                           30 June 2015
===================================================================
Features:
- Prior versions+
- Restructured framework (again), with run time enhancements
- Data files converted to JSON, removed python converters
- Enhancements to battle engine and graphics
- Can toggle sound in option menu
- Prof Elm intro scenes work
- Random wild pokemon works, and can battle
- Warnings hunt completed
- Graphics bugs on Unix-based OS fixed
- Deprecated unneeded classes

todo/KNOWN BUGS:
- See repository issues for more information

===================================================================
Version 1.1.0                                           23 Jan 2014
===================================================================
Features:
- Version 1.0.1 +
- Restructured framework, with run time enhancements
- Fully commented

todo:
- Options don't work
- pause menu selections only bare skeletons
- need world map sprite for pokegear
- wild encounters don't work
- need grass animation when walking through wild
- map should be expanded and should include indoors locations
- interact with world map (e.g. items, signs)
- prof elm's intro scenes

KNOWN BUGS:
- occasionally the player will be in the process of moving
	when a trainer sees them.  The behavior is a glitch, 
	the trainer should not move in to battle
===================================================================
Version 1.0.1                                           12 Jan 2014
===================================================================
Features:
- Version 1.0.0 +
- Pokemon database outside hard code
- Battle Engine works with placeholder moves
- Battle Engine accepts any Pokemon
- Pokemon handles sprite upgrade with level up and evolution
- Trainers' pokemon are included in npc factory to reduce hard code

todo:
- Options don't work
- pause menu selections only bare skeletons
- need world map sprite for pokegear
- wild encounters don't work
- need grass animation when walking through wild
- map should be expanded and should include indoors locations
- interact with world map (e.g. items, signs)
- prof elm's intro scenes
- ADD COMMENTS before it's too late!

KNOWN BUGS:
- occasionally the player will be in the process of moving
	when a trainer sees them.  The behavior is a glitch, 
	the trainer should not move in to battle	
===================================================================
Version 1.0.0                                           11 Jan 2014
===================================================================
- Initial clean "working" version.
Features:
- New Game Intro Scene w/ name selection
- Battle Engine works (hard coded battle)
- "Continue" hard coded values work
- Sprites library half cleaned

todo:
- battle engine picks default move for every turn
- Options don't work
- pause menu selections only bare skeletons
- need world map sprite for pokegear
- sprites in battles collide with frame b/c of sizing difference
- wild encounters don't work
- need grass animation when walking through wild
- map should be expanded and should include indoors locations
- interact with world map (e.g. items, signs)
- prof elm's intro scenes

KNOWN BUGS:
- occasionally the player will be in the process of moving
	when a trainer sees them.  The behavior is a glitch, 
	the trainer should not move in to battle
===================================================================
                     END RELEASE NOTES
===================================================================