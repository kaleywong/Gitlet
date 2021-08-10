package gitlet;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.Arrays;
import java.util.List;
import java.util.Iterator;
import java.util.SortedMap;
import java.util.TreeMap;

/** All the functions for gitlet.
 * @author kaleywong*/

public class Gitlet implements Serializable {

    /**Name of the file.*/
    private String fileName;
    /**Name of the initial commit.*/
    private String initcommit;
    /**Nme of the root directory.*/
    private String workingdirectory;
    /**Keeps track of untracked files.*/
    private ArrayList<String> untrackedFiles;
    /**Keeps track of the tracked files at any given point in time.*/
    private Set<String> trackedFiles;
    /** The staging area.*/
    private Stage stage;
    /**The names of all branches in the file.*/
    private ArrayList<String> branches;
    /**The is all the commits.*/
    private ArrayList<String> totalCommits;
    /**This is the branchheads mapping.*/
    private HashMap<String, String> bHeads;
    /**This is the currentbranch we are on.*/
    private String currBranch;
    /**This is the current commit hash val.*/
    private String header;
    /**This is the state of initalized.*/
    private boolean initialized;
    /** For Merge.*/
    private int low;
    /** For Merge.*/
    private String globalBalances;


    /**This initializes the Gitlet class.*/
    public Gitlet() {
        initialized = false;
        untrackedFiles = new ArrayList<String>();
        trackedFiles = new HashSet<String>();
        totalCommits = new ArrayList<String>();
        bHeads = new HashMap<String, String>();
        branches = new ArrayList<String>();
        stage = new Stage();
        fileName = ".gitlet" + System.getProperty("file.separator");
        workingdirectory = System.getProperty("user.dir");
    }

    /**This runs our gitlet.
     @param args this just has arguments*/
    public void run(String... args) throws IOException {
        ArrayList<String> group1 = new ArrayList<String>(Arrays.asList(
                "add", "rm", "commit", "init", "log", "global-log", "checkout"

        ));
        ArrayList<String> group2 = new ArrayList<String>(Arrays.asList(
                 "status", "branch", "rm-branch", "reset", "find", "merge"
        ));
        if (group1.contains(args[0])) {
            functions1(args);
        } else if (group2.contains(args[0])) {
            functions2(args);
        } else {
            throw new GitletException("No command with that name exists.");
        }
    }




    /**This runs add, commit, init, log, global log and checkout functions.
     @param args the arguments */
    public void functions1(String... args) throws IOException {
        if (args[0].equals("add")) {
            if (args.length == 2) {
                add(args[1]);
            } else {
                throw new GitletException("Incorrect Operands.");
            }
        } else if (args[0].equals("rm")) {
            if (args.length == 2) {
                rm(args[1]);
            } else {
                throw new GitletException("Incorrect Operands.");
            }
        } else if (args[0].equals("commit")) {
            if (args.length == 2) {
                commit(args[1]);
            } else {
                throw new GitletException("Incorrect Operands.");
            }
        } else if (args[0].equals("init")) {
            if (args.length == 1) {
                init();
            } else {
                throw new GitletException("Incorrect Operands.");
            }
        } else if (args[0].equals("log")) {
            if (args.length == 1) {
                log();
            } else {
                throw new GitletException("Incorrect Operands.");
            }
        } else if (args[0].equals("global-log")) {
            if (args.length == 1) {
                globalLog();
            } else {
                throw new GitletException("Incorrect Operands.");
            }
        } else if (args[0].equals("checkout")) {
            if (args[1].equals("--")) {
                if (args.length == 3) {
                    checkout(args[2], header);
                } else {
                    throw new GitletException("Incorrect Operands.");
                }

            } else if (args.length > 2 && args[2].equals("--")) {
                if (args.length == 4) {
                    checkout(args[3], args[1]);
                } else {
                    throw new GitletException("Incorrect Operands.");
                }
            } else if (args.length == 2) {
                checkoutb(args[1]);
            } else {
                throw new GitletException("Incorrect Operands.");
            }
        }
    }

    /**This runs status, branch, rm-branch, reset and find functions.
     @param args the arguments */
    public void functions2(String... args) {
        if (args[0].equals("status")) {
            if (args.length == 1) {
                status();
            } else {
                throw new GitletException("Incorrect Operands.");
            }
        } else if (args[0].equals("branch")) {
            if (args.length == 2) {
                branch(args[1]);
            } else {
                throw new GitletException("Incorrect Operands.");
            }
        } else if (args[0].equals("rm-branch")) {
            if (args.length == 2) {
                removebranch(args[1]);
            } else {
                throw new GitletException("Incorrect Operands.");
            }
        } else if (args[0].equals("reset")) {
            if (args.length == 2) {
                reset(args[1]);
            } else {
                throw new GitletException("Incorrect Operands.");
            }
        } else if (args[0].equals("find")) {
            if (args.length == 2) {
                find(args[1]);
            } else {
                throw new GitletException("Incorrect Operands.");
            }
        } else if (args[0].equals("merge")) {
            if (args.length == 2) {
                merge(args[1]);
            } else {
                throw new GitletException("Incorrect Operands.");
            }
        }


    }


    /**Init should creates a repository if one doesn't exist.*/
    public void init() throws IOException {
        if (initialized) {
            String error = "A Gitlet "
                    + "version-control system "
                    + "already exists in the current directory.";
            throw new GitletException(error);
        } else {
            initialized = true;
            Files.createDirectory(Paths.get(".gitlet"));
            Commit initial = new Commit();
            String initName = initial.gethash();
            totalCommits.add(initName);
            File loc = Utils.join(fileName, initName);
            Utils.writeObject(loc, initial);
            branches.add("master");
            currBranch = "master";
            low = Integer.MAX_VALUE;
            globalBalances = header;
            header = initName;
            bHeads.put("master", initName);
            initcommit = initName;
        }
    }

    /**This is the add function for the gitlet class.
     @param file the name of the file*/
    public void add(String file) {
        SomeObj blob;
        File relevant = new File(file);
        if (relevant.exists()) {
            blob = new SomeObj(file);
        } else {
            throw new GitletException("File does not exist");
        }
        trackedFiles.add(file);
        if (untrackedFiles.contains(file)) {
            untrackedFiles.remove(file);
        }
        String hashed = blob.getHash();
        File prev = Utils.join(fileName, header);
        Commit previous = Utils.readObject(prev, Commit.class);
        if (previous.getBlobs().contains(hashed)) {
            stage.remove(file);
        } else {
            stage.add(file);
        }
    }

    /**Removes a file.
     * @param file the name of the file*/
    public void rm(String file) {
        Commit x = Utils.readObject(
                Utils.join(fileName, header), Commit.class);
        if (x.getContents().containsKey(file) || stage.contains(file)) {
            if (x.getContents().containsKey(file)) {
                untrackedFiles.add(file);
                File temp = new File(file);
                Utils.restrictedDelete(temp);
            } else if (stage.contains(file)) {
                stage.remove(file);
            }
        } else {
            throw new GitletException("No reason to remove the file.");
        }
    }

    /**Make a new commit.
     @param message the message associated with the commit.*/
    public void commit(String message) {
        if (message.equals("")) {
            throw new GitletException("Please enter a commit message.");
        } else {
            File parent = Utils.join(fileName, header);
            Commit prevCommit = Utils.readObject(parent, Commit.class);
            HashMap<String, String> prev = prevCommit.getContents();
            HashMap<String, String> staging = stage.getContents();
            HashMap<String, String> cur = new HashMap<String, String>();
            for (String name : trackedFiles) {
                if (staging.containsKey(name)) {
                    cur.put(name, staging.get(name));
                } else if (!untrackedFiles.contains(name)
                        && prev.containsKey(name)) {
                    cur.put(name, prev.get(name));
                }
            }
            if (prev.equals(cur)) {
                throw new GitletException(" No changes added to the commit.");
            } else {
                stage = new Stage();
                Commit current = new Commit(message, cur, header, currBranch);
                String name = current.gethash();
                bHeads.put(currBranch, name);
                totalCommits.add(name);
                header = name;
                File loc = Utils.join(fileName, name);
                Utils.writeObject(loc, current);
                untrackedFiles.clear();
            }
        }
    }

    /**Displays the commits.*/
    public void log() {
        File x = Utils.join(fileName, header);
        Commit currentCommit = Utils.readObject(x, Commit.class);
        while (currentCommit.getParent() != null) {
            System.out.println("===");
            System.out.println("commit " + currentCommit.gethash());
            if (currentCommit.getSParent() != null) {
                String first = currentCommit.getParent().substring(0, 7);
                String second = currentCommit.getSParent().substring(0, 7);
                System.out.println("Merge: " + first + " " + second);
            }
            System.out.println("Date: " + currentCommit.getTime());
            System.out.println(currentCommit.getMessage());
            System.out.println();
            String temp = currentCommit.getParent();
            x = new File(fileName + temp);
            currentCommit = Utils.readObject(x, Commit.class);
        }
        System.out.println("===");
        System.out.println("commit " + currentCommit.gethash());
        System.out.println("Date: " + currentCommit.getTime());
        System.out.println(currentCommit.getMessage());
        System.out.println();
    }

    /**Calls all the logs.*/
    public void globalLog() {
        for (String b : totalCommits) {
            File x = new File(fileName + b);
            Commit currentCommit = Utils.readObject(x, Commit.class);
            System.out.println("===");
            System.out.println("commit " + currentCommit.gethash());
            if (currentCommit.getSParent() != null) {
                String first = currentCommit.getParent().substring(0, 6);
                String second = currentCommit.getSParent().substring(0, 6);
                System.out.println("Merge: " + first + " " + second);
            }
            System.out.println("Date: " + currentCommit.getTime());
            System.out.println(currentCommit.getMessage());
            System.out.println();
        }
    }

    /**This will find a specific commit message.
     * @param message the message*/
    public void find(String message) {
        int found = 1;
        for (String b : totalCommits) {
            File cur = new File(fileName + b);
            Commit current = Utils.readObject(cur, Commit.class);
            String currentMessage = current.getMessage();
            if (message.equals(currentMessage)) {
                System.out.println(current.gethash());
                found = 2;
            }
        }
        if (found == 1) {
            throw new GitletException("Found no commit with that message.");
        }
    }


    /**
     * This will print out the status of the entire thing.
     */
    public void status() {
        List<String> workingDir = Utils.plainFilenamesIn(
                workingdirectory);
        Commit curr = Utils.readObject(
                Utils.join(workingdirectory, header), Commit.class);
        System.out.println("=== Branches ===");
        SortedMap<String, String> sorted = new TreeMap<
                String, String>(bHeads);
        for (String b : sorted.keySet()) {
            if (b.equals(currBranch)) {
                System.out.println("*" + b);
            } else {
                System.out.println(b);
            }
        }
        System.out.println();
        System.out.println("=== Staged Files ===");
        for (String b : stage.getContents().keySet()) {
            System.out.println(b);
        }
        System.out.println();
        System.out.println("=== Removed Files ===");
        for (String b : untrackedFiles) {
            System.out.println(b);
        }
        System.out.println();
        System.out.println("=== Modifications Not Staged For Commit ===");
        for (String file : curr.getContents().keySet()) {
            if (workingDir.contains(file)) {
                SomeObj temp = new SomeObj(file);
                if (!temp.getHash().equals(curr.getContents().get(
                        file)) && !stage.contains(file)) {
                    System.out.println(file + " (modified)");
                }
            } else {
                if (!untrackedFiles.contains(file)) {
                    System.out.println(file + " (deleted)");
                }
            }
        }
        for (String file : stage.getContents().keySet()) {
            if (workingDir.contains(file)) {
                SomeObj temp = new SomeObj(file);
                if (!temp.getHash().equals(
                        stage.getContents().get(file))) {
                    System.out.println(file + " (modified)");
                }
            } else {
                System.out.println(file + " (deleted)");
            }
        }
        System.out.println();
        System.out.println("=== Untracked Files ===");
        for (String b : workingDir) {
            if (!trackedFiles.contains(b) && !stage.contains(b)) {
                System.out.println(b);
            }
        }
        System.out.println();
    }



    /**
     * This will change the file to the file we want.
     *
     * @param name   the name of the file
     * @param commit the commit of the file
     */
    public void checkout(String name, String commit) {
        if (commit.length() >= header.length()) {
            File file = new File(fileName + commit);
            if (!file.exists()) {
                throw new GitletException("No commit with that id exists.");
            }
            Commit head = Utils.readObject(file, Commit.class);
            String hash = head.getContents().get(name);
            if (head.getContents().containsKey(name)) {
                file = new File(fileName + hash);
                SomeObj x = Utils.readObject(file, SomeObj.class);
                file = new File(name);
                Utils.writeContents(file, x.getBContents());
            } else {
                throw new GitletException("File does not"
                        + "exist in that commit.");
            }
        } else if (commit.length() < header.length()) {
            boolean x = false;
            Iterator a = totalCommits.iterator();
            for (String b : totalCommits) {
                if (commit.equals(b.substring(0, commit.length()))) {
                    checkout(name, b);
                    x = true;
                }
            }
            if (!x) {
                throw new GitletException("No commit with that id exists.");
            }
        }
    }


    /**Checkout a branch.
     @param branchName the name of the branch*/
    public void checkoutb(String branchName) {
        branchName = branchName.replace("/", File.separator);
        if (bHeads.keySet().contains(branchName)) {
            if (branchName.equals(currBranch)) {
                throw new GitletException("No need to"
                        + "checkout the current branch.");
            } else {
                File current = new File(
                        fileName + bHeads.get(branchName));
                Commit branch = Utils.readObject(current, Commit.class);
                HashMap<String, String> files = branch.getContents();
                if (!stage.getContents().isEmpty()) {
                    throw new GitletException("You have uncommitted changes.");
                }
                List<String> workingDir = Utils.plainFilenamesIn(
                        workingdirectory);
                Iterator<String> a = trackedFiles.iterator();
                while (a.hasNext()) {
                    if (!trackedFiles.contains(a)
                            && files.keySet().contains(a)) {
                        throw new GitletException("There "
                                + "is an untracked file in the "
                                + "way; delete it or add it first.");
                    }
                }
                for (String c : trackedFiles) {
                    if (!files.containsKey(c)) {
                        Utils.restrictedDelete(c);
                    }
                }
                trackedFiles.clear();
                for (String b : files.keySet()) {
                    String hash = files.get(b);
                    SomeObj c = Utils.readObject(
                            new File(fileName + hash), SomeObj.class);
                    Utils.writeContents(new File(b), c.getBContents());
                    trackedFiles.add(b);
                }
                currBranch = branchName;
                header = bHeads.get(branchName);
                stage.clear();
            }
        } else {
            throw new GitletException("No such branch exists.");
        }
    }

    /**
     * This is how we add a branch.
     *
     * @param name the name of the branch
     */
    public void branch(String name) {
        if (branches.contains(name)) {
            throw new GitletException("branch with that name already exists.");
        } else {
            bHeads.put(name, header);
            branches.add(name);
        }
    }

    /**
     * This removes a branch.
     *
     * @param name the name of the branch to remove
     */
    public void removebranch(String name) {
        if (!branches.contains(name)) {
            throw new GitletException("A branch with"
                    + "that name does not exist.");
        } else if (name.equals(currBranch)) {
            throw new GitletException("Cannot remove the current branch.");
        } else {
            bHeads.remove(name);
            branches.remove(name);
        }
    }

    /**
     * This is how we reset to a previous commit.
     *
     * @param commmitID a string for hash value of commit to reset to
     */
    public void reset(String commmitID) {
        if (totalCommits.contains(commmitID)) {
            Commit temp = Utils.readObject(new File(
                    fileName + commmitID), Commit.class);
            HashMap<String, String> files = temp.getContents();
            List<String> workingDir = Utils.plainFilenamesIn(workingdirectory);
            for (String ter : workingDir) {
                if (!trackedFiles.contains(ter)
                        && files.keySet().contains(ter)) {
                    throw new GitletException("There "
                            + "is an untracked file in "
                            + "the way; delete it or add it first.");
                }
            }
            for (String ter : workingDir) {
                if (trackedFiles.contains(ter)
                        && !files.keySet().contains(ter)) {
                    new File(ter).delete();
                }
            }
            trackedFiles.clear();
            for (String b : files.keySet()) {
                trackedFiles.add(b);
                checkout(b, commmitID);
            }
            stage.clear();
            header = commmitID;
            bHeads.put(currBranch, commmitID);
        } else {
            throw new GitletException("No commit with that id exists.");
        }
    }

    /**This is a helper function for merge.
     * @param first  the first
     * @param branch the branch
     * @param total  the total*/
    public void helper(String first, String branch, int total) {
        Commit temp = Utils.readObject(
                Utils.join(workingdirectory, first), Commit.class);
        if (temp.getbranch().equals(branch) && (total < low)) {
            low = total;
            globalBalances = first;

        } else if (temp.getParent() != null) {
            if (temp.getSParent() != null) {
                helper(temp.getSParent(), branch, total + 1);
            }
            helper(temp.getParent(), branch, total + 1);
        }
    }

    /**Locates the parents.
     * @param first     the first
     * @param second     the second
     * @param bran1 the branch1
     * @param bran2 the branch2
     * @return the String */
    public String locator(
            String first, String second, String bran1, String bran2) {
        helper(first, bran2, 0);
        helper(second, bran1, 0);
        return globalBalances;
    }

    /**
     * This is a helper function.
     * @param allfiles    this is allfiles
     * @param givenValue   the given values
     * @param ancestorValue the ancestor values
     * @param currentValue  the current values
     * @param branName      the branch name
     * @param given       the commit of the given
     * @return boolean whether it was a conflict*/

    public boolean helper2(HashSet<String> allfiles,
                                   HashMap<String, String> givenValue,
                                   HashMap<String, String> ancestorValue,
                                   HashMap<String, String> currentValue,
                                   String branName,
                                   Commit given) {
        boolean conflict = false;
        for (String c : allfiles) {
            if (givenValue.containsKey(c)) {
                if (ancestorValue.get(c) != null && !givenValue.get(
                        c).equals(ancestorValue.get(c))
                        && currentValue.get(c).equals(ancestorValue.get(c))) {
                    checkout(c, given.gethash());
                    stage.add(c);
                } else if (!ancestorValue.containsKey(c)
                        && !currentValue.containsKey(c)) {
                    checkout(c, given.gethash());
                    stage.add(c);
                } else if (currentValue.containsKey(c)
                        && !currentValue.get(c).equals(givenValue.get(c))) {
                    SomeObj x = Utils.readObject(
                            Utils.join(workingdirectory, currentValue.get(c)),
                            SomeObj.class);
                    SomeObj sec = Utils.readObject(
                            Utils.join(workingdirectory, givenValue.get(c)),
                            SomeObj.class);
                    String concat = "<<<<<<< HEAD\n"
                            + x.getSContents()
                            + "=======\n" + sec.getSContents()
                            + ">>>>>>>\n";
                    Utils.writeContents(new File(c), concat);
                    add(c);
                    conflict = true;
                } else if (!currentValue.containsKey(c)
                        && !givenValue.get(c).equals(ancestorValue.get(c))) {
                    SomeObj sec = Utils.readObject(
                            Utils.join(workingdirectory, givenValue.get(c)),
                            SomeObj.class);
                    String concat = "<<<<<<< HEAD\n"
                            + "=======\n" + sec.getSContents()
                            + ">>>>>>>\n";
                    Utils.writeContents(new File(c), concat);
                    add(c);
                    conflict = true;
                }

            } else {
                if (ancestorValue.containsKey(c)) {
                    if (currentValue.containsKey(c)
                            && currentValue.get(c).equals(
                            ancestorValue.get(c))) {
                        new File(c).delete();
                        untrackedFiles.add(c);
                    } else if (currentValue.containsKey(c)
                            && !currentValue.get(c).equals(
                            ancestorValue.get(c))) {
                        helper3(currentValue, branName, c);
                        conflict = true;
                    }
                }
            }
        }
        return conflict;
    }

    /**This is how this works.
     * @param currentvalue a hashmap
     * @param branName branch name
     * @param name the name of value*/
    private void helper3(HashMap<String, String> currentvalue,
                               String branName,
                               String name) {
        SomeObj fir = Utils.readObject(
                Utils.join(workingdirectory, currentvalue.get(currentvalue)),
                SomeObj.class);
        String concat = "<<<<<<< HEAD\n"
                + fir.getSContents()
                + "=======\n"
                + ">>>>>>>\n";
        Utils.writeContents(new File(name), concat);
        add(name);
    }


    /** This is the merge function.
     * @param branName the name of the branch to merge*/
    public void merge(String branName) {
        String givenHashcode = bHeads.get(branName);
        String currentHashcode = bHeads.get(currBranch);
        String old = locator(
                givenHashcode, currentHashcode, branName, currentHashcode);
        Commit ancestorCommit = Utils.readObject(
                Utils.join(workingdirectory, old), Commit.class);
        Commit givenCommit = Utils.readObject(
                Utils.join(workingdirectory, givenHashcode), Commit.class);
        Commit currentCommit = Utils.readObject(
                Utils.join(workingdirectory, currentHashcode), Commit.class);
        if (ancestorCommit.gethash().equals(givenHashcode)) {
            throw new GitletException("Given branch is an ancestor");
        } else if (ancestorCommit.gethash().equals(currentHashcode)) {
            bHeads.put(currentHashcode, givenHashcode);
            throw new GitletException("Current branch fast-forwarded.");
        }
        low = Integer.MAX_VALUE;
        globalBalances = header;
        HashSet<String> allfiles = new HashSet<String>();
        for (String b : ancestorCommit.getContents().keySet()) {
            allfiles.add(b);
        }
        for (String b : givenCommit.getContents().keySet()) {
            allfiles.add(b);
        }
        for (String b : currentCommit.getContents().keySet()) {
            allfiles.add(b);
        }
        HashMap<String, String> givenValue = givenCommit.getContents();
        HashMap<String, String> currentValue = currentCommit.getContents();
        HashMap<String, String> ancestorValue = ancestorCommit.getContents();
        boolean conflict = helper2(allfiles, givenValue, ancestorValue,
                currentValue, branName, givenCommit);
        if (conflict) {
            System.out.println("Encountered a merge conflict.");
        }
        HashMap<String, String> staging = stage.getContents();
        HashMap<String, String> curr = new HashMap<String, String>();
        for (String name : allfiles) {
            if (staging.containsKey(name)) {
                trackedFiles.add(name);
                curr.put(name, staging.get(name));
            } else if (!untrackedFiles.contains(name)
                    && currentValue.containsKey(name)) {
                trackedFiles.add(name);
                curr.put(name, currentValue.get(name));
            }
        }
        Commit merged = new Commit(
                currentCommit.gethash(), givenCommit.gethash(),
                curr, currBranch, branName);
        stage = new Stage();
        String name = merged.gethash();
        bHeads.put(currentHashcode, name);
        totalCommits.add(name);
        header = name;
        Utils.writeObject(Utils.join(workingdirectory, name), merged);
        untrackedFiles.clear();
    }











}

