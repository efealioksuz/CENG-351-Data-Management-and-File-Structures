import java.util.ArrayList;

public class PlaylistTree {
	
	public PlaylistNode primaryRoot;		//root of the primary B+ tree
	public PlaylistNode secondaryRoot;	//root of the secondary B+ tree
	public PlaylistTree(Integer order) {
		PlaylistNode.order = order;
		primaryRoot = new PlaylistNodePrimaryLeaf(null);
		primaryRoot.level = 0;
		secondaryRoot = new PlaylistNodeSecondaryLeaf(null);
		secondaryRoot.level = 0;
	}
	
	public void addSong(CengSong song) {
		// TODO: Implement this method
		// add methods to fill both primary and secondary tree

		PlaylistNodePrimaryLeaf leaf_to_insert_p = find_leaf_primary(this.primaryRoot, song.audioId());
		int index_to_insert_p = find_index_primary_leaf(leaf_to_insert_p, song.audioId());

		leaf_to_insert_p.addSong(index_to_insert_p, song);

		if(leaf_to_insert_p.songCount() == 2*(PlaylistNode.order) + 1){
			ArrayList<CengSong> leaf_songs = leaf_to_insert_p.getSongs();

			if(leaf_to_insert_p.getParent() == null){
				ArrayList<CengSong> songs1 = new ArrayList<CengSong>(leaf_songs.subList(0,PlaylistNode.order));
				ArrayList<CengSong> songs2 = new ArrayList<CengSong>(leaf_songs.subList(PlaylistNode.order,2*PlaylistNode.order+1));
				leaf_songs.clear();

				PlaylistNodePrimaryLeaf leaf1 = new PlaylistNodePrimaryLeaf(null, songs1);
				PlaylistNodePrimaryLeaf leaf2 = new PlaylistNodePrimaryLeaf(null, songs2);

				ArrayList<Integer> new_root_audioID = new ArrayList<Integer>();
				new_root_audioID.add((songs2.get(0)).audioId());

				ArrayList<PlaylistNode> new_root_children = new ArrayList<PlaylistNode>();
				new_root_children.add(leaf1);
				new_root_children.add(leaf2);
				this.primaryRoot = new PlaylistNodePrimaryIndex(null, new_root_audioID, new_root_children);

				leaf1.setParent(this.primaryRoot);
				leaf2.setParent(this.primaryRoot);
			}
			else{
				ArrayList<CengSong> songs1 = new ArrayList<CengSong>(leaf_songs.subList(0,PlaylistNode.order));
				ArrayList<CengSong> songs2 = new ArrayList<CengSong>(leaf_songs.subList(PlaylistNode.order,2*PlaylistNode.order+1));
				leaf_songs.clear();

				PlaylistNodePrimaryLeaf leaf1 = new PlaylistNodePrimaryLeaf(null, songs1);
				PlaylistNodePrimaryLeaf leaf2 = new PlaylistNodePrimaryLeaf(null, songs2);
				Integer audioIDtoCopy = (songs2.get(0)).audioId();

				int index = find_index_primary_internal((PlaylistNodePrimaryIndex) leaf_to_insert_p.getParent(), audioIDtoCopy);
				((PlaylistNodePrimaryIndex) leaf_to_insert_p.getParent()).addAudioID(index, audioIDtoCopy);

				((PlaylistNodePrimaryIndex) leaf_to_insert_p.getParent()).setChildren(index, leaf1);
				((PlaylistNodePrimaryIndex) leaf_to_insert_p.getParent()).addChildren(index+1, leaf2);

				leaf1.setParent(leaf_to_insert_p.getParent());
				leaf2.setParent(leaf_to_insert_p.getParent());

				if(((PlaylistNodePrimaryIndex) leaf_to_insert_p.getParent()).audioIdCount() == 2*(PlaylistNode.order) + 1){
					pushUpPrimary((PlaylistNodePrimaryIndex) leaf_to_insert_p.getParent());
				}
			}
		}


		PlaylistNodeSecondaryLeaf leaf_to_insert_s = find_leaf_secondary(this.secondaryRoot, song.genre());
		int index_to_insert_s = find_index_secondary_leaf(leaf_to_insert_s, song.genre());

		leaf_to_insert_s.addSong(index_to_insert_s, song);

		if(leaf_to_insert_s.genreCount() == 2*(PlaylistNode.order) + 1){
			ArrayList<ArrayList<CengSong>> leaf_buckets = leaf_to_insert_s.getSongBucket();

			if(leaf_to_insert_s.getParent() == null){
				ArrayList<ArrayList<CengSong>> bucket1 = new ArrayList<ArrayList<CengSong>>(leaf_buckets.subList(0,PlaylistNode.order));
				ArrayList<ArrayList<CengSong>> bucket2 = new ArrayList<ArrayList<CengSong>>(leaf_buckets.subList(PlaylistNode.order,2*PlaylistNode.order+1));
				leaf_buckets.clear();

				PlaylistNodeSecondaryLeaf leaf1 = new PlaylistNodeSecondaryLeaf(null, bucket1);
				PlaylistNodeSecondaryLeaf leaf2 = new PlaylistNodeSecondaryLeaf(null, bucket2);

				ArrayList<String> new_root_genre = new ArrayList<String>();
				new_root_genre.add(((bucket2.get(0)).get(0)).genre());

				ArrayList<PlaylistNode> new_root_children = new ArrayList<PlaylistNode>();
				new_root_children.add(leaf1);
				new_root_children.add(leaf2);
				this.secondaryRoot = new PlaylistNodeSecondaryIndex(null, new_root_genre, new_root_children);

				leaf1.setParent(this.secondaryRoot);
				leaf2.setParent(this.secondaryRoot);
			}
			else{
				ArrayList<ArrayList<CengSong>> bucket1 = new ArrayList<ArrayList<CengSong>>(leaf_buckets.subList(0,PlaylistNode.order));
				ArrayList<ArrayList<CengSong>> bucket2 = new ArrayList<ArrayList<CengSong>>(leaf_buckets.subList(PlaylistNode.order,2*PlaylistNode.order+1));
				leaf_buckets.clear();

				PlaylistNodeSecondaryLeaf leaf1 = new PlaylistNodeSecondaryLeaf(null, bucket1);
				PlaylistNodeSecondaryLeaf leaf2 = new PlaylistNodeSecondaryLeaf(null, bucket2);
				String genreToCopy = ((bucket2.get(0)).get(0)).genre();

				int index = find_index_secondary_internal((PlaylistNodeSecondaryIndex) leaf_to_insert_s.getParent(), genreToCopy);
				((PlaylistNodeSecondaryIndex) leaf_to_insert_s.getParent()).addGenre(index, genreToCopy);

				((PlaylistNodeSecondaryIndex) leaf_to_insert_s.getParent()).setChildren(index, leaf1);
				((PlaylistNodeSecondaryIndex) leaf_to_insert_s.getParent()).addChildren(index+1, leaf2);

				leaf1.setParent(leaf_to_insert_s.getParent());
				leaf2.setParent(leaf_to_insert_s.getParent());

				if(((PlaylistNodeSecondaryIndex) leaf_to_insert_s.getParent()).genreCount() == 2*(PlaylistNode.order) + 1){
					pushUpSecondary((PlaylistNodeSecondaryIndex) leaf_to_insert_s.getParent());
				}
			}
		}

		return;
	}
	
	public CengSong searchSong(Integer audioId) {
		// TODO: Implement this method
		// find the song with the searched audioId in primary B+ tree
		// return value will not be tested, just print according to the specifications
		searchSongHelper(primaryRoot, audioId, 0);
		return null;
	}
	
	
	public void printPrimaryPlaylist() {
		// TODO: Implement this method
		// print the primary B+ tree in Depth-first order
		printPrimaryPlaylistHelper(primaryRoot, 0);
		return;
	}
	
	public void printSecondaryPlaylist() {
		// TODO: Implement this method
		// print the secondary B+ tree in Depth-first order
		printSecondaryPlaylistHelper(secondaryRoot, 0);
		return;
	}
	
	// Extra functions if needed

	public PlaylistNodePrimaryLeaf find_leaf_primary(PlaylistNode root, Integer audioID){
		if(root.getType() == PlaylistNodeType.Leaf){
			return (PlaylistNodePrimaryLeaf) root;
		}
		else{
			int i = 0;
			while(((PlaylistNodePrimaryIndex) root).audioIdAtIndex(i) < audioID && i < (((PlaylistNodePrimaryIndex) root).audioIdCount()-1)){
				i++;
			}
			if(i == (((PlaylistNodePrimaryIndex) root).audioIdCount()-1) && ((PlaylistNodePrimaryIndex) root).audioIdAtIndex(i) < audioID){
				i++;
			}
			root = ((PlaylistNodePrimaryIndex) root).getChildrenAt(i);
			return find_leaf_primary(root,audioID);
		}
	}

	public int find_index_primary_leaf(PlaylistNode leaf, Integer audioID){
		int i = 0;
		while (((PlaylistNodePrimaryLeaf) leaf).audioIdAtIndex(i) < audioID && i < (((PlaylistNodePrimaryLeaf) leaf).songCount() - 1)) {
			i++;
		}
		if (i == (((PlaylistNodePrimaryLeaf) leaf).songCount() - 1) && ((PlaylistNodePrimaryLeaf) leaf).audioIdAtIndex(i) < audioID) {
			i++;
		}

		return i;
	}

	public int find_index_primary_internal(PlaylistNode internal, Integer audioID){
		int i = 0;
		while(((PlaylistNodePrimaryIndex) internal).audioIdAtIndex(i) < audioID && i < (((PlaylistNodePrimaryIndex) internal).audioIdCount()-1)){
			i++;
		}
		if(i == (((PlaylistNodePrimaryIndex) internal).audioIdCount()-1) && ((PlaylistNodePrimaryIndex) internal).audioIdAtIndex(i) < audioID){
			i++;
		}
		return i;
	}

	public void pushUpPrimary(PlaylistNodePrimaryIndex internal){
		if(internal.getParent() == null){
			Integer audioIDToPushUp = internal.audioIdAtIndex(PlaylistNode.order);
			ArrayList<Integer> audioIDsToPushUp = new ArrayList<Integer>();
			audioIDsToPushUp.add(audioIDToPushUp);

			ArrayList<Integer> audioIDs1 = new ArrayList<Integer>((internal.getAllAudioIDs()).subList(0,PlaylistNode.order));
			ArrayList<Integer> audioIDs2 = new ArrayList<Integer>((internal.getAllAudioIDs()).subList(PlaylistNode.order+1,2*PlaylistNode.order+1));
			(internal.getAllAudioIDs()).clear();

			ArrayList<PlaylistNode> children1 = new ArrayList<PlaylistNode>((internal.getAllChildren()).subList(0,PlaylistNode.order+1));
			ArrayList<PlaylistNode> children2 = new ArrayList<PlaylistNode>((internal.getAllChildren()).subList(PlaylistNode.order+1,2*PlaylistNode.order+2));
			(internal.getAllChildren()).clear();

			PlaylistNodePrimaryIndex internal_child1 = new PlaylistNodePrimaryIndex(null, audioIDs1, children1);
			PlaylistNodePrimaryIndex internal_child2 = new PlaylistNodePrimaryIndex(null, audioIDs2, children2);

			for(int i=0; i<children1.size(); i++){
				(children1.get(i)).setParent(internal_child1);
			}

			for(int i=0; i<children2.size(); i++){
				(children2.get(i)).setParent(internal_child2);
			}

			ArrayList<PlaylistNode> new_children = new ArrayList<PlaylistNode>();
			new_children.add(internal_child1);
			new_children.add(internal_child2);

			PlaylistNodePrimaryIndex new_root = new PlaylistNodePrimaryIndex(null, audioIDsToPushUp, new_children);

			internal_child1.setParent(new_root);
			internal_child2.setParent(new_root);

			this.primaryRoot = new_root;
		}
		else{
			Integer audioIDToPushUp = internal.audioIdAtIndex(PlaylistNode.order);
			int index = find_index_primary_internal(internal.getParent(), audioIDToPushUp);
			((PlaylistNodePrimaryIndex) internal.getParent()).addAudioID(index, audioIDToPushUp);

			ArrayList<Integer> audioIDs1 = new ArrayList<Integer>((internal.getAllAudioIDs()).subList(0,PlaylistNode.order));
			ArrayList<Integer> audioIDs2 = new ArrayList<Integer>((internal.getAllAudioIDs()).subList(PlaylistNode.order+1,2*PlaylistNode.order+1));
			(internal.getAllAudioIDs()).clear();

			ArrayList<PlaylistNode> children1 = new ArrayList<PlaylistNode>((internal.getAllChildren()).subList(0,PlaylistNode.order+1));
			ArrayList<PlaylistNode> children2 = new ArrayList<PlaylistNode>((internal.getAllChildren()).subList(PlaylistNode.order+1,2*PlaylistNode.order+2));
			(internal.getAllChildren()).clear();

			PlaylistNodePrimaryIndex internal_child1 = new PlaylistNodePrimaryIndex(internal.getParent(), audioIDs1, children1);
			PlaylistNodePrimaryIndex internal_child2 = new PlaylistNodePrimaryIndex(internal.getParent(), audioIDs2, children2);

			for(int i=0; i<children1.size(); i++){
				(children1.get(i)).setParent(internal_child1);
			}

			for(int i=0; i<children2.size(); i++){
				(children2.get(i)).setParent(internal_child2);
			}

			((PlaylistNodePrimaryIndex) internal.getParent()).setChildren(index, internal_child1);
			((PlaylistNodePrimaryIndex) internal.getParent()).addChildren(index+1, internal_child2);

			if(((PlaylistNodePrimaryIndex) internal.getParent()).audioIdCount() == 2*(PlaylistNode.order) + 1){
				pushUpPrimary((PlaylistNodePrimaryIndex) internal.getParent());
			}
		}
	}


	public PlaylistNodeSecondaryLeaf find_leaf_secondary(PlaylistNode root, String genre){
		if(root.getType() == PlaylistNodeType.Leaf){
			return (PlaylistNodeSecondaryLeaf) root;
		}
		else{
			int i = 0;
			while((((PlaylistNodeSecondaryIndex) root).genreAtIndex(i)).compareTo(genre) <= 0 && i < (((PlaylistNodeSecondaryIndex) root).genreCount()-1) ){
				i++;
			}
			if(i == (((PlaylistNodeSecondaryIndex) root).genreCount()-1) && (((PlaylistNodeSecondaryIndex) root).genreAtIndex(i)).compareTo(genre) <= 0 ){
				i++;
			}
			root = ((PlaylistNodeSecondaryIndex) root).getChildrenAt(i);
			return find_leaf_secondary(root, genre);
		}
	}

	public int find_index_secondary_leaf(PlaylistNode leaf, String genre){
		int i = 0;
		if(((PlaylistNodeSecondaryLeaf) leaf).genreAtIndex(i) != null){
			while ((((PlaylistNodeSecondaryLeaf) leaf).genreAtIndex(i)).compareTo(genre) < 0 && i < (((PlaylistNodeSecondaryLeaf) leaf).genreCount() - 1)) {
				i++;
			}
			if (i == (((PlaylistNodeSecondaryLeaf) leaf).genreCount() - 1) && (((PlaylistNodeSecondaryLeaf) leaf).genreAtIndex(i)).compareTo(genre) < 0) {
				i++;
			}
		}
		return i;
	}

	public int find_index_secondary_internal(PlaylistNode internal, String genre){
		int i = 0;
		while((((PlaylistNodeSecondaryIndex) internal).genreAtIndex(i)).compareTo(genre) < 0 && i < (((PlaylistNodeSecondaryIndex) internal).genreCount()-1)){
			i++;
		}
		if(i == (((PlaylistNodeSecondaryIndex) internal).genreCount()-1) && (((PlaylistNodeSecondaryIndex) internal).genreAtIndex(i)).compareTo(genre) < 0){
			i++;
		}
		return i;
	}

	public void pushUpSecondary(PlaylistNodeSecondaryIndex internal){
		if(internal.getParent() == null){
			String genreToPushUp = internal.genreAtIndex(PlaylistNode.order);
			ArrayList<String> genresToPushUp = new ArrayList<String>();
			genresToPushUp.add(genreToPushUp);

			ArrayList<String> genres1 = new ArrayList<String>((internal.getAllGenres()).subList(0,PlaylistNode.order));
			ArrayList<String> genres2 = new ArrayList<String>((internal.getAllGenres()).subList(PlaylistNode.order+1,2*PlaylistNode.order+1));
			(internal.getAllGenres()).clear();

			ArrayList<PlaylistNode> children1 = new ArrayList<PlaylistNode>((internal.getAllChildren()).subList(0,PlaylistNode.order+1));
			ArrayList<PlaylistNode> children2 = new ArrayList<PlaylistNode>((internal.getAllChildren()).subList(PlaylistNode.order+1,2*PlaylistNode.order+2));
			(internal.getAllChildren()).clear();

			PlaylistNodeSecondaryIndex internal_child1 = new PlaylistNodeSecondaryIndex(null, genres1, children1);
			PlaylistNodeSecondaryIndex internal_child2 = new PlaylistNodeSecondaryIndex(null, genres2, children2);

			for(int i=0; i<children1.size(); i++){
				(children1.get(i)).setParent(internal_child1);
			}

			for(int i=0; i<children2.size(); i++){
				(children2.get(i)).setParent(internal_child2);
			}

			ArrayList<PlaylistNode> new_children = new ArrayList<PlaylistNode>();
			new_children.add(internal_child1);
			new_children.add(internal_child2);

			PlaylistNodeSecondaryIndex new_root = new PlaylistNodeSecondaryIndex(null, genresToPushUp, new_children);

			internal_child1.setParent(new_root);
			internal_child2.setParent(new_root);

			this.secondaryRoot = new_root;
		}
		else{
			String genreToPushUp = internal.genreAtIndex(PlaylistNode.order);
			int index = find_index_secondary_internal(internal.getParent(), genreToPushUp);
			((PlaylistNodeSecondaryIndex) internal.getParent()).addGenre(index, genreToPushUp);

			ArrayList<String> genres1 = new ArrayList<String>((internal.getAllGenres()).subList(0,PlaylistNode.order));
			ArrayList<String> genres2 = new ArrayList<String>((internal.getAllGenres()).subList(PlaylistNode.order + 1, 2 * PlaylistNode.order + 1));
			(internal.getAllGenres()).clear();

			ArrayList<PlaylistNode> children1 = new ArrayList<PlaylistNode>((internal.getAllChildren()).subList(0,PlaylistNode.order+1));
			ArrayList<PlaylistNode> children2 = new ArrayList<PlaylistNode>((internal.getAllChildren()).subList(PlaylistNode.order+1,2*PlaylistNode.order+2));
			(internal.getAllChildren()).clear();

			PlaylistNodeSecondaryIndex internal_child1 = new PlaylistNodeSecondaryIndex(internal.getParent(), genres1, children1);
			PlaylistNodeSecondaryIndex internal_child2 = new PlaylistNodeSecondaryIndex(internal.getParent(), genres2, children2);

			for(int i=0; i<children1.size(); i++){
				(children1.get(i)).setParent(internal_child1);
			}

			for(int i=0; i<children2.size(); i++){
				(children2.get(i)).setParent(internal_child2);
			}

			((PlaylistNodeSecondaryIndex) internal.getParent()).setChildren(index, internal_child1);
			((PlaylistNodeSecondaryIndex) internal.getParent()).addChildren(index+1, internal_child2);

			if(((PlaylistNodeSecondaryIndex) internal.getParent()).genreCount() == 2*(PlaylistNode.order) + 1){
				pushUpSecondary((PlaylistNodeSecondaryIndex) internal.getParent());
			}
		}
	}


	public void searchSongHelper(PlaylistNode node, Integer audioID, int level){
		if(node.getType() == PlaylistNodeType.Leaf){
			CengSong song = null;
			for(int i=0; i < ((PlaylistNodePrimaryLeaf) node).songCount(); i++){
				if(audioID == ((PlaylistNodePrimaryLeaf) node).audioIdAtIndex(i)){
					song = ((PlaylistNodePrimaryLeaf) node).songAtIndex(i);
				}
			}

			if(song == null){
				System.out.print("Could not find " );
				System.out.println(audioID);
			}
			else {
				System.out.println("\t".repeat(level) + "<data>");
				System.out.print("\t".repeat(level) + "<record>");
				System.out.print(song.audioId());
				System.out.print("|");
				System.out.print(song.genre());
				System.out.print("|");
				System.out.print(song.songName());
				System.out.print("|");
				System.out.print(song.artist());
				System.out.println("</record>");
				System.out.println("\t".repeat(level) + "</data>");
			}
		}
		else{
			System.out.println("\t".repeat(level) + "<index>");
			PlaylistNode new_node = ((PlaylistNodePrimaryIndex) node).getChildrenAt(0);;
			for(int i=0; i < ((PlaylistNodePrimaryIndex) node).audioIdCount(); i++){
				if(((PlaylistNodePrimaryIndex) node).audioIdAtIndex(i) <= audioID){
					new_node = ((PlaylistNodePrimaryIndex) node).getChildrenAt(i+1);
				}
				System.out.print("\t".repeat(level));
				System.out.println(((PlaylistNodePrimaryIndex) node).audioIdAtIndex(i));
			}
			System.out.println("\t".repeat(level) + "</index>");
			searchSongHelper(new_node, audioID, level+1 );
		}
	}

	public void printPrimaryPlaylistHelper(PlaylistNode node, int level){
		if(node.getType() == PlaylistNodeType.Leaf){
			System.out.println("\t".repeat(level) + "<data>");
			for(int i=0; i < ((PlaylistNodePrimaryLeaf) node).songCount(); i++){
				CengSong song = ((PlaylistNodePrimaryLeaf) node).songAtIndex(i);
				System.out.print("\t".repeat(level) + "<record>");
				System.out.print(song.audioId());
				System.out.print("|");
				System.out.print(song.genre());
				System.out.print("|");
				System.out.print(song.songName());
				System.out.print("|");
				System.out.print(song.artist());
				System.out.println("</record>");
			}
			System.out.println("\t".repeat(level) + "</data>");
		}
		else{
			System.out.println("\t".repeat(level) + "<index>");
			for(int i=0; i < ((PlaylistNodePrimaryIndex) node).audioIdCount(); i++){
				System.out.print("\t".repeat(level));
				System.out.println(((PlaylistNodePrimaryIndex) node).audioIdAtIndex(i));
			}
			System.out.println("\t".repeat(level) + "</index>");

			for(int i=0; i <((PlaylistNodePrimaryIndex) node).childrenCount(); i++){
				printPrimaryPlaylistHelper(((PlaylistNodePrimaryIndex) node).getChildrenAt(i), level + 1);
			}
		}
	}

	public void printSecondaryPlaylistHelper(PlaylistNode node, int level){
		if(node.getType() == PlaylistNodeType.Leaf){
			ArrayList<ArrayList<CengSong>> bucket = ((PlaylistNodeSecondaryLeaf) node).getSongBucket();
			System.out.println("\t".repeat(level) + "<data>");
			for(int i=0; i < ((PlaylistNodeSecondaryLeaf) node).genreCount(); i++){
				System.out.print("\t".repeat(level));
				System.out.println(((PlaylistNodeSecondaryLeaf) node).genreAtIndex(i));
				ArrayList<CengSong> songList = bucket.get(i);
				for(int k=0; k < songList.size(); k++){
					CengSong song = songList.get(k);
					System.out.print("\t".repeat(level+1) + "<record>");
					System.out.print(song.audioId());
					System.out.print("|");
					System.out.print(song.genre());
					System.out.print("|");
					System.out.print(song.songName());
					System.out.print("|");
					System.out.print(song.artist());
					System.out.println("</record>");
				}
			}
			System.out.println("\t".repeat(level) + "</data>");
		}
		else{
			System.out.println("\t".repeat(level) + "<index>");
			for(int i=0; i < ((PlaylistNodeSecondaryIndex) node).genreCount(); i++){
				System.out.print("\t".repeat(level));
				System.out.println(((PlaylistNodeSecondaryIndex) node).genreAtIndex(i));
			}
			System.out.println("\t".repeat(level) + "</index>");

			for(int i=0; i <((PlaylistNodeSecondaryIndex) node).childrenCount(); i++){
				printSecondaryPlaylistHelper(((PlaylistNodeSecondaryIndex) node).getChildrenAt(i), level + 1);
			}
		}
	}
}