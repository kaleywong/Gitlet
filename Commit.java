package gitlet;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Collection;

/** Commit Class.
 * @author kaleywong
 */

public class Commit implements Serializable {

    /** If it merges or not..*/
    private Boolean merge;
    /** Name of the branch. */
    private String _branch;
    /** Name of merge parent. */
    private String _mergeparent;
    /** Parent hashcode value. */
    private String _parent;
    /** Time of the commit. */
    private String time;
    /** Hashmap with filename and files. */
    private HashMap<String, String> branches;
    /** The message of the commit. */
    private String _message;

    /** Normal initialization.
     * @param message the message for the commit
     * @param files the contents
     * @param parent the parent of the shit
     * @param branch the branch it belongs to*/
    public Commit(
            String message,
            HashMap<String, String> files, String parent, String branch) {
        branches = files;
        _message = message;
        merge = false;
        _parent = parent;
        _branch = branch;
        _mergeparent = null;
        Date date = new Date();
        SimpleDateFormat format = new SimpleDateFormat(
                "E MMM dd HH:mm:ss yyyy Z");
        time = format.format(date);
    }
    /** Initialization of a merged commit..
     * @param firstParent the first parent
     * @param secondParent the second parent
     * @param files the second set of files
     * @param branch1 the branch it belongs to
     * @param branch2 the branch it belongs to */

    public Commit(
            String firstParent,
            String secondParent, HashMap<String, String>
                    files, String branch1, String branch2) {
        _message  = "Merged " + branch2 + " into " + branch1 + ".";
        _parent = firstParent;
        _mergeparent = secondParent;
        merge = true;
        branches = files;
        _branch = branch1;
        Date date = new Date();
        SimpleDateFormat format = new SimpleDateFormat(
                "E MMM dd HH:mm:ss yyyy Z");
        time = format.format(date);
    }
    /** Copies commit.
     * @param prevCommit the other hash value of the commit*/
    public Commit(Commit prevCommit) {
        _message = prevCommit.getMessage();
        _parent = prevCommit.getParent();
        _mergeparent = prevCommit.getSParent();
        merge = prevCommit.merge;
        branches = prevCommit.getContents();
        _branch = prevCommit.getbranch();
        time = prevCommit.getTime();
    }

    /** Put an element in the contents.
     * @param name the name of the thing to put in
     * @param hashcode the hash value of the value*/
    public void put(String name, String hashcode) {
        branches.put(name, hashcode);
    }
    /** Remove an element from the contents.
     * @param name the name of the file to remove. */
    public void rm(String name) {
        branches.remove(name);
    }

    /** Used to create the initial commit. */
    public Commit() {
        _parent = null;
        merge = false;
        _mergeparent = null;
        branches = new HashMap<String, String>();
        _branch = "master";
        Date date = new Date(0);
        SimpleDateFormat format = new SimpleDateFormat(
                "E MMM dd HH:mm:ss yyyy Z");
        time = format.format(date);
        _message = "initial commit";
    }
    /** This is the way of retrieves the hash.
     * @return the string of the hash */
    private String hashval() {
        List<Object> total = new ArrayList<Object>();
        total.add(_message);
        total.add(time);
        total.add(_branch);
        total.add("Commit");
        if (_parent == null) {
            return Utils.sha1(total);
        } else if (merge) {
            total.add(_mergeparent);
            for (String b: branches.values()) {
                total.add(b);
            }
            return Utils.sha1(total);
        } else {
            for (String b: branches.values()) {
                total.add(b);
            }
            return Utils.sha1(total);
        }
    }
    /** Return the hash value.
     * @return String of the hash */
    String gethash() {
        return hashval();
    }
    /** Return the branch this commit belongs too.
     * @return name of the branch */
    public String getbranch() {
        return _branch;
    }
    /** Get the message of the commit.
     * @return the message*/
    public String getMessage() {
        return _message;
    }
    /** Get the time of the commit.
     * @return the time of the commit*/
    public String getTime() {
        return time;
    }
    /** Retrieves name of the parent.
     * @return the parent*/
    public String getParent() {
        return _parent;
    }
    /** Retrieves name of the second parent.
     * @return the mergedparent. */
    public String getSParent() {
        return _mergeparent;
    }
    /** Retrieves the hashes of the blobs.
     * @return the collection */
    public Collection<String> getBlobs() {
        return branches.values();
    }
    /** Retrieves the contents of the commit.
     * @return hashMap */
    public HashMap<String, String> getContents() {
        return branches;
    }

}
