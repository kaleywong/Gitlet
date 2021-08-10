# Gitlet Design Document

**Name**: Kaley Wong

## Classes and Data Structures
### Commit
* Boolean Merge - returns true or false if the commit merges or not. 
* String _branch - the name of the branch.
* String _mergeparent - the name of the parent merge.
* String _parent  -  the parent hashcode ID.
* String time - the time of the commit.
* String message - the message of the commit.
* Hashmap branches - a hashmap for the branches that contain the file and file names.
### SomeObj
* String name - the name of the file.
* contentsbyte= it get the contents of that byte array.
* String content - the contents of the file.
* String hashcode - the hashcode value of the file.
### Gitlet
* String fileName - the name of the file.
* String initcommit - the name of the initial commit.
* String workingdirectory - the name of the main directory.
* ArrayList untrackedFiles - keeps track of all the files that are untracked.
* Set trackedFiles - keeps track of all the files that have been tracked already.
* ArrayList branches - contains all the files.
* ArrayList totalCommits - contains all the commits that have been made.
* Hashmap bHeads - keeps track of the branches head.
* String currBranch - keeps track of the current branch.
* String header - this is most current commit id.
* Boolean initialized - returns true or false if it has been initialized already.


## Algorithms
### SomeObj
* getName - gets the name of the file.
* getSContents - gets the name as a string.
* getBContents - get the file as a byte array.
* gethash - gets the hash code.


### Commit 
* commit - initializes a regular commit.
* commit - initializes a merged commit.
* commit - copied the commit.
* commit - used to create the initial commit.
* hashval - it gets the hashcode ID.
* gethash - gets the hashcode valude.
* getbranch - gets the branch this commit belongs too.
* getMessage - gets the message of this commit.
* getTime - gets the time of the commit.
* getContents - gets the contents of the commit. 

###Gitlet
* runs - runs the gitlet functions.
* group 1 - a helper function run a group of commands.
* group 2 - a helper function that runs a group of commands.
* init - this initializes the gitlet directory 
*  add - this stages a file to the staging area, and is helped along with the
* remove - removes the file from the staging area and even remove it from the directory if it was tracked
* commit - creates a commit object from the variables passed, staging area and the tracked array
  list and then adds this to the relevant data structures and such
* log - displays the ancestry of the current commit essentially
* globalLog - shows all the commits
* find - will find a commit based on a message
* status - will print out the entire status including the modifications, the names, etc
* checkout - this mimcs the functionality of the checkout with a file name and the name of a commit
  and it involves checking to see if the length of the string is 40 characters or not and than finding the
  relevant commit and checking out the file
 * shortenCommitId - shortens the commidID length
 * helper2 - 
 * branch - this creates a new branch
 * rmbranch - this is the opposite of the add branch and will take away a branch if is exists where it needs to be
 * reset - the implementation for this is identical to checkout branch, with the exception that it also changes
   the current branch head

## Persistence

