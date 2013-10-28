BEncode Editor Readme
-=-=-=-=-=-=-=-=-=-=-

Introduction
============
Many people have previously asked about where one might find an editor that can
safely edit BEncoded files, but none have really existed in the general sense.
The only ones I've seen were .torrent file editors, which (although they
technically do edit BEncoded files) don't edit just ANY type of BEncoded file.
Out of a bit of boredom and free time, this editor was born.

Warning
=======
Consider this editor to be experimental. As such, you should exercise caution by
backing up any files you plan on editing with this utility.

Uses
====
 * Edit .torrent files:
   - the announce/announce-list keys (binary/list) store the tracker(s)
   - the url-list key (binary/list) stores the webseed(s)
   - the nodes key (list) stores the DHT bootstrap node(s)
   WARNING: When editing .torrent files, any changes to the info dictionary will
            change the .torrent file's infohash. Unless you know what you're
            doing, you should refrain from doing this. If you aren't familiar
            with the .torrent file metadata structure, read this:
            http://wiki.theory.org/BitTorrentSpecification
            (in particular, the "Metainfo File Structure" section)

 * Edit uTorrent's .dat files:
   - corrupt resume.dat files can sometimes be salvaged simply by loading and
     saving the resume.dat file in this editor
   - the paths stored in resume.dat can be edited en masse with Find/Replace
   WARNING: Make sure you first exit uTorrent before editing these files, as
            uTorrent rewrites/updates the files on exit. Additionally, the
            .fileguard key should be removed, since uTorrent will consider the
            edited .dat file to be damaged if it is edited and no longer matches
            the stored .fileguard hash.

Notes
=====
 * Binary data and integers are exported as raw data rather than BEncoded data
 * Dictionary merging does not sort keys or resolve duplicate keys
 * File recovery recovers only decodable parts of input files
 * Finding "Value by Key" finds exact key names unless RegEx is used
 * Holding Ctrl while reloading reloads data from disk
 * Holding Shift while moving moves an item to top/bottom (direction-dependent)
 * Holding Shift while pasting pastes the item below the currently focused item
 * Holding Shift while sorting will recursively search for dictionaries to sort
 * Only dictionaries can be sorted
 * Only files containing dictionaries and lists can be edited directly
 * Switching an item between dictionary/list will cause the item to be cleared
 * The root of a dictionary (not its children) must be selected to sort it

History
=======
v0.7.1.0 (2010-02-09)
 + Feature: Recovery fallback on decoding error

v0.7.0.0 (2009-12-31)
 ^ New: x64 build support
 + Feature: Item hashing
 + Feature: Item validation
 + Feature: Merge binary/dictionary/list data
 + Feature: Move item to top/bottom
 + Feature: Recursive sorting (hold Shift while clicking Item > Sort Keys)
 + Feature: Undo/redo changes
 ~ Change: Allow blank key names when searching using "Value by Key"
 ~ Change: Allow save if file no longer exists, even without changes
 ~ Change: Binary input/conversion made slightly more lenient/reliable
 ~ Change: Exporting integers exports a raw (rather than BEncoded) integer
 ~ Change: Indicate in the window title whether a file has been modified
 ~ Change: Move up/down keyboard shortcuts changed to Ctrl+Up/Down
 ~ Change: Reload the treeview on F5, reload from disk on Ctrl+R
 ~ Change: Select all text in focused dialog input control on Ctrl+A
 ~ Change: Shift+F3 searches in the opposite direction
 ~ Change: Show "Find" dialog if never shown before "Find Next" is used
 ~ Change: Store Find options only on search, not on dialog close
 * Fix: Repeated successive conversions into Binary allowed in Find/Replace
 * Fix: Switching data types not properly hiding/showing controls in dialogs
 * Fix: UI "allows" child to be added to an integer, but crashes on attempt

v0.6.1.0 (2008-09-18)
 * Fix: Paths with consecutive '%' characters fail to open

v0.6.0.0 (2008-06-20)
 + Feature: Cut/Copy/Paste items
 + Feature: Filter searches based on key
 + Feature: Holding Ctrl while reloading reloads data from disk
 + Feature: Log the number of replacements made during Replace All
 + Feature: Move item up/down
 + Feature: On import, use filename as suggestion for key name (if empty)
 + Feature: Regular expressions search/replace (PCRE engine)
 + Feature: Select all Logger tab items with Ctrl+A
 + Feature: Sort keys (for dictionaries only)
 ~ Change: Add accelerators for dialog buttons
 ~ Change: Center popup dialogs on display
 ~ Change: Disable dialog buttons on action
 ~ Change: Display Edit dialog on double-click only if text double-clicked
 ~ Change: Don't collapse/expand treeview item on double-click
 ~ Change: Faster BDecoding (~30% improvement over v0.5.1 in tests)
 ~ Change: Faster BEncoding (~85% improvement over v0.5.1 in tests)
 ~ Change: Faster Find/Replace operation (much improved on larger treeviews)
 ~ Change: Faster treeview filling (~50% improvement over v0.5.1 in tests)
 ~ Change: Focus main window on drag-and-drop
 ~ Change: Increase input limit on edit controls
 ~ Change: Searches for blank values are always treated as exact searches
 ~ Change: Select all text in Value field each time Find/Replace dialog shown
 ~ Change: Unicode support on treeview and listview
 ~ Change: Use accelerators instead of global hotkeys
 ~ Change: Visual feedback when busy performing find (busy mouse cursor)
 * Fix: Inaccurate window resize limiting under various Windows themes
 * Fix: Non-transparent background on "button" icons in certain situations
 * Fix: Switching binary input type in Add/Edit dialogs causes data loss
 * Fix: Sub-item count not displayed for dictionaries/lists added to a list

v0.5.1.0 (2008-03-06)
 * Fix: Missing controls in the Find/Replace dialog

v0.5.0.0 (2008-02-14)
 + Feature: "Item" menu with associated keyboard shortcuts
 + Feature: Copy Logger tab items with Ctrl+C
 + Feature: Double-click to edit item
 + Feature: Export data from selected item
 + Feature: Import external file as binary data
 + Feature: Input raw BEncoded data directly or from an external file
 ~ Change: Add .torrent and .dat to dropdown list in Open/Save dialogs
 ~ Change: Add line breaks to the Logger tab between file opens
 ~ Change: Disable hotkeys when busy
 ~ Change: Don't allow saves when no changes have been made to data
 ~ Change: Faster BDecoding (~15% improvement over v0.4.1 in tests)
 ~ Change: Faster treeview filling (~20% improvement over v0.4.1 in tests)
 ~ Change: Miscellaneous Find/Replace tweaks, fixes, and polish
 ~ Change: Show type and item count for "[ ROOT ]" item
 * Fix: "ITEM -1" when adding child list/dictionary to bottom of list
 * Fix: "Reached beginning/end of document" message sometimes shown twice
 * Fix: Combobox not showing options in dropdown on pre-XP systems
 * Fix: Strings can replace integers in Find/Replace
 * Fix: Typo in Logger entry for BEncoding
 * Fix: Using "Find Next" sometimes returns focus to wrong window

v0.4.1.0 (2007-12-05)
 * Fix: "Properize" bad integer input

v0.4.0.0 (2007-12-05)
 ~ Change: "File > Load" changed to "File > Open"
 ~ Change: Faster BDecoding (~30% improvement over v0.3 in tests)
 ~ Change: Faster treeview filling (~70% improvement over v0.3 in tests)
 ~ Change: Minor optimizations to adding/deleting/editing of items
 ~ Change: No longer clears window if file loading/decoding fails
 ~ Change: No longer switches to Structure tab on file open
 ~ Change: Scroll Logger when new entries are added
 ~ Change: Timings shown in Logger
 * Fix: File locked if open/save fail
 * Fix: File save error caused window to clear (possible data loss)
 * Fix: Find/Replace on keys with dictionary values caused incorrect item count
 * Fix: Integers in lists not displayed on treeview
 * Fix: Integers larger than 32-bits not displayed properly on treeview
 * Fix: Integers larger than 64-bits unusable
 * Fix: Keyboard shortcut for file open not working
 * Fix: Making binary value empty in lists doesn't update/show on treeview

v0.3.0.0 (2007-11-25)
 + Feature: Drag and drop support
 + Feature: Find/Find Next/Replace
 + Feature: Load files from commandline
 ~ Change: A bit more aware of disk read/write errors
 ~ Change: Don't close main window on Esc
 ~ Change: Faster BDecoding (~75% improvement over v0.2 in tests)
 ~ Change: Log some more events
 ~ Change: Smaller executable due to lessened dependence on standard library
 ~ Change: Visual feedback when busy (busy mouse cursor)

v0.2.0.0 (2007-10-25)
 + Feature: Create NEW BEncoded files
 + Feature: Edit "[ ROOT ]" item directly
 + Feature: Keyboard shortcuts
 ~ Change: Ask to save file before performing actions where changes get lost
 ~ Change: Don't display ellipses ("...") for empty binary strings
 ~ Change: Improve string/binary conversion to minimize chances of data loss
 ~ Change: Switch to Logger tab on error
 ~ Change: Use child dialogs for FileOpenDialog, FileSaveDialog, and MsgBox
 ~ Change: Use icons as buttons (Crystal Clear icon set by Everaldo)
 ~ Change: Warn when data loss imminent from switching views
 * Fix: Saving appends data to end of file

v0.1.0.0 (2007-10-08)
 ^ New: Initial release

Credits
=======
Everaldo (Crystal Clear icon set)
