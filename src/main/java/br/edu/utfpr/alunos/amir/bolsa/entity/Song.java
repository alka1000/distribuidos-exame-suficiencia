package br.edu.utfpr.alunos.amir.bolsa.entity;

public class Song {

    private String title;
    private String artist;
    private long lengthMillis;
    
    
    public Song() {
    }
    
    public Song(String title, String artist, long lengthMillis) {
        this.title = title;
        this.artist = artist;
        this.lengthMillis = lengthMillis;
    }

    public void setTitle(String title) {
		this.title = title;
	}

	public void setArtist(String artist) {
		this.artist = artist;
	}

	public void setLengthMillis(long lengthMillis) {
		this.lengthMillis = lengthMillis;
	}

	public String getTitle() {
        return title;
    }

    public String getArtist() {
        return artist;
    }

    public long getLengthMillis() {
        return lengthMillis;
    }

    @Override
    public String toString() {
        return title + "|" + artist + "|" + lengthMillis;
    }
}
