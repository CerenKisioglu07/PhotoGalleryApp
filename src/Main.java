import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class Main extends JFrame {
    private DefaultListModel<Photo> photoListModel;
    private JList<Photo> photoList;
    private JLabel photoLabel;
    private JTextArea tagsArea;
    private JTextArea detailsArea;
    private JButton addTagButton;
    private JButton removeTagButton;
    private DefaultListModel<Collection> collectionListModel;
    private JList<Collection> collectionList;
    private JList<Photo> collectionPhotoList;
    private DefaultListModel<Photo> collectionPhotoListModel;
    private JLabel collectionPhotoLabel;
    private JTextField searchField;
    private JButton searchButton;

    public Main() {
        setTitle("Photo Gallery");
        setSize(1200, 800);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JTabbedPane tabbedPane = new JTabbedPane();

        JPanel photoGalleryPanel = new JPanel(new BorderLayout());

        photoListModel = new DefaultListModel<>();
        photoList = new JList<>(photoListModel);
        photoList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        photoList.addListSelectionListener(e -> showSelectedPhoto());

        photoLabel = new JLabel();
        photoLabel.setHorizontalAlignment(JLabel.CENTER);

        tagsArea = new JTextArea(2, 20);
        tagsArea.setEditable(false);
        JScrollPane tagsScrollPane = new JScrollPane(tagsArea);

        detailsArea = new JTextArea(3, 20);
        detailsArea.setEditable(false);
        JScrollPane detailsScrollPane = new JScrollPane(detailsArea);

        // Initialize tag buttons
        addTagButton = new JButton("Add Tag");
        addTagButton.addActionListener(new AddTagAction());
        addTagButton.setVisible(false);

        removeTagButton = new JButton("Remove Tag");
        removeTagButton.addActionListener(new RemoveTagAction());
        removeTagButton.setVisible(false);

        JPanel tagButtonPanel = new JPanel();
        tagButtonPanel.add(addTagButton);
        tagButtonPanel.add(removeTagButton);

        JPanel infoPanel = new JPanel(new BorderLayout());
        infoPanel.add(detailsScrollPane, BorderLayout.NORTH);
        infoPanel.add(tagsScrollPane, BorderLayout.CENTER);
        infoPanel.add(tagButtonPanel, BorderLayout.SOUTH);

        photoGalleryPanel.add(new JScrollPane(photoList), BorderLayout.WEST);
        photoGalleryPanel.add(photoLabel, BorderLayout.CENTER);
        photoGalleryPanel.add(infoPanel, BorderLayout.SOUTH);

        // Add photo button
        JButton addButton = new JButton("Add Photo");
        addButton.addActionListener(new AddPhotoAction());

        // Add search field and button
        searchField = new JTextField(20);
        searchButton = new JButton("Search");
        searchButton.addActionListener(new SearchAction());

        JPanel searchPanel = new JPanel();
        searchPanel.add(new JLabel("Search:"));
        searchPanel.add(searchField);
        searchPanel.add(searchButton);

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(addButton, BorderLayout.WEST);
        topPanel.add(searchPanel, BorderLayout.EAST);

        photoGalleryPanel.add(topPanel, BorderLayout.NORTH);

        tabbedPane.addTab("Photo Gallery", photoGalleryPanel);

        JPanel collectionsPanel = new JPanel(new BorderLayout());

        collectionListModel = new DefaultListModel<>();
        collectionList = new JList<>(collectionListModel);
        collectionList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        collectionList.addListSelectionListener(e -> showSelectedCollection());

        collectionPhotoListModel = new DefaultListModel<>();
        collectionPhotoList = new JList<>(collectionPhotoListModel);
        collectionPhotoList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        collectionPhotoList.addListSelectionListener(e -> showSelectedCollectionPhoto());

        collectionPhotoLabel = new JLabel();
        collectionPhotoLabel.setHorizontalAlignment(JLabel.CENTER);

        JPanel collectionPhotoPanel = new JPanel(new BorderLayout());
        collectionPhotoPanel.add(collectionPhotoLabel, BorderLayout.CENTER);

        JSplitPane collectionSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, new JScrollPane(collectionPhotoList), collectionPhotoPanel);
        collectionSplitPane.setDividerLocation(300);

        JButton addCollectionButton = new JButton("Add Collection");
        addCollectionButton.addActionListener(new AddCollectionAction());

        JButton addPhotoToCollectionButton = new JButton("Add Photo to Collection");
        addPhotoToCollectionButton.addActionListener(new AddPhotoToCollectionAction());

        JPanel collectionButtonPanel = new JPanel();
        collectionButtonPanel.add(addCollectionButton);
        collectionButtonPanel.add(addPhotoToCollectionButton);

        collectionsPanel.add(new JScrollPane(collectionList), BorderLayout.WEST);
        collectionsPanel.add(collectionSplitPane, BorderLayout.CENTER);
        collectionsPanel.add(collectionButtonPanel, BorderLayout.NORTH);

        tabbedPane.addTab("Collections", collectionsPanel);

        add(tabbedPane, BorderLayout.CENTER);

        Dimension leftPanelSize = new Dimension(300, getHeight());
        photoList.setPreferredSize(leftPanelSize);
        collectionList.setPreferredSize(leftPanelSize);
    }

    private void showSelectedPhoto() {
        Photo selectedPhoto = photoList.getSelectedValue();
        if (selectedPhoto != null) {
            ImageIcon icon = new ImageIcon(selectedPhoto.getImageFile().getAbsolutePath());
            photoLabel.setIcon(icon);
            tagsArea.setText("Tags: " + String.join(", ", selectedPhoto.getTags()));
            detailsArea.setText("Date: " + new SimpleDateFormat("yyyy-MM-dd").format(selectedPhoto.getDate()) + "\nDescription: " + selectedPhoto.getDescription());
            addTagButton.setVisible(true);
            removeTagButton.setVisible(true);
        } else {
            photoLabel.setIcon(null);
            tagsArea.setText("");
            detailsArea.setText("");
            addTagButton.setVisible(false);
            removeTagButton.setVisible(false);
        }
    }

    private void showSelectedCollection() {
        Collection selectedCollection = collectionList.getSelectedValue();
        if (selectedCollection != null) {
            collectionPhotoListModel.clear();
            for (Photo photo : selectedCollection.getPhotos()) {
                collectionPhotoListModel.addElement(photo);
            }
        } else {
            collectionPhotoListModel.clear();
        }
    }

    private void showSelectedCollectionPhoto() {
        Photo selectedPhoto = collectionPhotoList.getSelectedValue();
        if (selectedPhoto != null) {
            ImageIcon icon = new ImageIcon(selectedPhoto.getImageFile().getAbsolutePath());
            collectionPhotoLabel.setIcon(icon);
        } else {
            collectionPhotoLabel.setIcon(null);
        }
    }

    private class AddPhotoAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
            int result = fileChooser.showOpenDialog(Main.this);
            if (result == JFileChooser.APPROVE_OPTION) {
                File selectedFile = fileChooser.getSelectedFile();
                String title = JOptionPane.showInputDialog("Enter photo title:");
                String tagsInput = JOptionPane.showInputDialog("Enter photo tags (comma separated):");
                String dateInput = JOptionPane.showInputDialog("Enter photo date (yyyy-MM-dd):");
                String description = JOptionPane.showInputDialog("Enter photo description:");

                if (title != null && !title.trim().isEmpty() && dateInput != null && !dateInput.trim().isEmpty() && description != null && !description.trim().isEmpty()) {
                    try {
                        Date date = new SimpleDateFormat("yyyy-MM-dd").parse(dateInput);
                        Photo photo = new Photo(title, selectedFile, date, description);
                        if (tagsInput != null) {
                            String[] tags = tagsInput.split(",");
                            for (String tag : tags) {
                                photo.addTag(tag.trim());
                            }
                        }
                        photoListModel.addElement(photo);
                    } catch (ParseException ex) {
                        JOptionPane.showMessageDialog(Main.this, "Invalid date format. Please use yyyy-MM-dd.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        }
    }

    private class AddTagAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            Photo selectedPhoto = photoList.getSelectedValue();
            if (selectedPhoto != null) {
                String newTag = JOptionPane.showInputDialog("Enter new tag:");
                if (newTag != null && !newTag.trim().isEmpty()) {
                    selectedPhoto.addTag(newTag.trim());
                    tagsArea.setText("Tags: " + String.join(", ", selectedPhoto.getTags()));
                }
            } else {
                JOptionPane.showMessageDialog(Main.this, "No photo selected", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private class RemoveTagAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            Photo selectedPhoto = photoList.getSelectedValue();
            if (selectedPhoto != null) {
                String tagToRemove = JOptionPane.showInputDialog("Enter tag to remove:");
                if (tagToRemove != null && !tagToRemove.trim().isEmpty()) {
                    selectedPhoto.removeTag(tagToRemove.trim());
                    tagsArea.setText("Tags: " + String.join(", ", selectedPhoto.getTags()));
                }
            } else {
                JOptionPane.showMessageDialog(Main.this, "No photo selected", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private class AddCollectionAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String collectionName = JOptionPane.showInputDialog("Enter collection name:");
            if (collectionName != null && !collectionName.trim().isEmpty()) {
                Collection collection = new Collection(collectionName);
                collectionListModel.addElement(collection);
            }
        }
    }

    private class AddPhotoToCollectionAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            Collection selectedCollection = collectionList.getSelectedValue();
            if (selectedCollection != null) {
                // Show a dialog to select photos to add to the collection
                JList<Photo> photoSelectionList = new JList<>(photoListModel);
                JScrollPane scrollPane = new JScrollPane(photoSelectionList);
                int result = JOptionPane.showConfirmDialog(Main.this, scrollPane, "Select Photos to Add to Collection", JOptionPane.OK_CANCEL_OPTION);
                if (result == JOptionPane.OK_OPTION) {
                    for (Photo photo : photoSelectionList.getSelectedValuesList()) {
                        selectedCollection.addPhoto(photo);
                    }
                    showSelectedCollection();
                    JOptionPane.showMessageDialog(Main.this, "Photos added to collection: " + selectedCollection.getName(), "Success", JOptionPane.INFORMATION_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(Main.this, "No collection selected", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private class SearchAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String query = searchField.getText().toLowerCase();
            DefaultListModel<Photo> searchResults = new DefaultListModel<>();
            for (int i = 0; i < photoListModel.size(); i++) {
                Photo photo = photoListModel.getElementAt(i);
                if (photo.getTitle().toLowerCase().contains(query) ||
                        photo.getDescription().toLowerCase().contains(query) ||
                        photo.getTags().stream().anyMatch(tag -> tag.toLowerCase().contains(query))) {
                    searchResults.addElement(photo);
                }
            }
            photoList.setModel(searchResults);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new Main().setVisible(true);
        });
    }
}