package it.polimi.ingsw.view.GUI;

import javax.swing.*;
import java.awt.*;
import java.awt.image.ImageObserver;
import java.awt.image.ImageProducer;
import java.util.HashMap;
import java.util.Map;

/**
 * Class wich contains the more images of the app, they will be used by GUI
 * */
public class ImageContainer {

    private Map<String, Image> gods;
    private Map<String, Image> buttons;
    private Image blueLight;
    private Image borderGod;
    private Image purpleLight;
    private Image[] towerImage = new Image[4];
    private Image[] imageColorPlayer = new Image[3];
    private Image imageLabelTerminalPlayerNick;
    private Image imageWinner;
    private Image imageClose;
    private Image imageDone;


    public ImageContainer(){
        gods = new HashMap<>();
        buttons = new HashMap<>();

        Image apollo = new ImageIcon(this.getClass().getResource("/god_cards/Apollo.png")).getImage();
        Image artemis = new ImageIcon(this.getClass().getResource("/god_cards/Artemis.png")).getImage();
        Image athena = new ImageIcon(this.getClass().getResource("/god_cards/Athena.png")).getImage();
        Image atlas = new ImageIcon(this.getClass().getResource("/god_cards/Atlas.png")).getImage();
        Image demeter = new ImageIcon(this.getClass().getResource("/god_cards/Demeter.png")).getImage();
        Image hephaestus = new ImageIcon(this.getClass().getResource("/god_cards/Hephaestus.png")).getImage();
        Image minotaur = new ImageIcon(this.getClass().getResource("/god_cards/Minotaur.png")).getImage();
        Image pan = new ImageIcon(this.getClass().getResource("/god_cards/Pan.png")).getImage();
        Image prometheus = new ImageIcon(this.getClass().getResource("/god_cards/Prometheus.png")).getImage();
        Image zeus = new ImageIcon(this.getClass().getResource("/god_cards/Zeus.png")).getImage();
        Image hestia = new ImageIcon(this.getClass().getResource("/god_cards/Hestia.png")).getImage();
        Image triton = new ImageIcon(this.getClass().getResource("/god_cards/Triton.png")).getImage();
        Image charon = new ImageIcon(this.getClass().getResource("/god_cards/Charon.png")).getImage();
        Image hera = new ImageIcon(this.getClass().getResource("/god_cards/Hera.png")).getImage();

        //Buttons image

        Image powerButton = new ImageIcon(this.getClass().getResource("/heropower_active_small.png")).getImage();
        Image endTurnButton = new ImageIcon(this.getClass().getResource("/Chronus_ClockFace.png")).getImage();
        Image tutorialButton = new ImageIcon(this.getClass().getResource("/TutorialOn.png")).getImage();
        Image menuButton = new ImageIcon(this.getClass().getResource("/menu_button.png")).getImage();


        Image tower1 = new ImageIcon(this.getClass().getResource("/frame_blue.png")).getImage();
        Image tower2 = new ImageIcon(this.getClass().getResource("/frame_coral.png")).getImage();
        Image tower3 = new ImageIcon(this.getClass().getResource("/frame_yellow.png")).getImage();
        Image tower4 = new ImageIcon(this.getClass().getResource("/Graeae_EyePupil.png")).getImage();

        Image imageColorBlue = new ImageIcon(this.getClass().getResource("/cm_btn_blue.png")).getImage();
        Image imageColorRed = new ImageIcon(this.getClass().getResource("/cm_btn_coral.png")).getImage();
        Image imageColorGray = new ImageIcon(this.getClass().getResource("/cm_btn_gray.png")).getImage();
        imageClose = new ImageIcon(this.getClass().getResource("/Destroy.png")).getImage();
        imageDone = new ImageIcon(this.getClass().getResource("/Done.png")).getImage();

        imageWinner =  new ImageIcon(this.getClass().getResource("/endgame_victorywin.png")).getImage();
        imageLabelTerminalPlayerNick = new ImageIcon(this.getClass().getResource("/cl_bg.png")).getImage();

        imageColorPlayer[0] = imageColorBlue;
        imageColorPlayer[1] = imageColorRed;
        imageColorPlayer[2] = imageColorGray;

        towerImage[0] = tower1;
        towerImage[1] = tower2;
        towerImage[2] = tower3;
        towerImage[3] = tower4;

        gods.put("Apollo", apollo);
        gods.put("Artemis", artemis);
        gods.put("Athena", athena);
        gods.put("Atlas", atlas);
        gods.put("Demeter", demeter);
        gods.put("Hephaestus", hephaestus);
        gods.put("Minotaur", minotaur);
        gods.put("Pan", pan);
        gods.put("Prometheus", prometheus);
        gods.put("Zeus", zeus);
        gods.put("Hestia", hestia);
        gods.put("Triton", triton);
        gods.put("Charon", charon);
        gods.put("Hera", hera);

        buttons.put("buttonPower",powerButton);
        buttons.put("buttonEndTurn",endTurnButton);
        buttons.put("buttonTutorial",tutorialButton);
        buttons.put("buttonMenu",menuButton);


        blueLight = new ImageIcon(this.getClass().getResource("/playermoveindicator_blue.png")).getImage();
        borderGod = new ImageIcon(this.getClass().getResource("/clp_frame_gold.png")).getImage();
        purpleLight = new ImageIcon(this.getClass().getResource("/playermoveindicator_purple.png")).getImage();





    }

    protected Image getGodimage(String godName){
        return gods.get(godName);
    }

    protected Image getButtonImage(String typeButton){ return buttons.get(typeButton);}

    protected Image getBlueLight(){
        return blueLight;
    }

    protected Image getBorderGod(){
        return borderGod;
    }

    protected Image getPurpleLight(){ return purpleLight;}

    protected Image getTowerLevel(int level){
        return towerImage[level];
    }

    protected Image getButtonColorPlayer(int numPlayerIndex) { return imageColorPlayer[numPlayerIndex];}

    protected Image getPlayersTerminalNick () { return imageLabelTerminalPlayerNick;}

    protected Image getImageWinner () { return imageWinner;}

    protected Image getCloseImage () { return imageClose;}

    protected Image getDoneImage () { return imageDone;}
}