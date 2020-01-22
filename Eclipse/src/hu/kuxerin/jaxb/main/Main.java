package hu.kuxerin.jaxb.main;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;

import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.PropertyException;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import hu.kuxerin.jaxb.generated.ROOT;
import hu.kuxerin.jaxb.generated.ROOT.Authors.Author;
import hu.kuxerin.jaxb.generated.ROOT.Books.Book;
import hu.kuxerin.jaxb.generated.ROOT.Books.Book.Copies;
import hu.kuxerin.jaxb.generated.ROOT.Books.Book.Copies.Copy;

public class Main {
	private static final String XML_DOCUMENT_FILE = "src/hu/kuxerin/jaxb/xml/Document.xml";
	private static final String XML_ORIGINAL_DOCUMENT_FILE = "src/hu/kuxerin/jaxb/xml/Document_original.xml";
	private static final String XML_SCHEMA_FILE = "src/hu/kuxerin/jaxb/xsd/XSD_final.xsd";

	public static void main(String[] args) {
		try {
			// reset xml from backup so no need for replacing xml before 2nd run
			resetXML();

			Schema schema = loadSchema();

			JAXBContext jaxbContext = JAXBContext.newInstance(ROOT.class);
			Unmarshaller unmarshaller = createUnmarshaller(schema, jaxbContext);
			Marshaller marshaller = createMarshaller(schema, jaxbContext);

			ROOT root = loadXML(unmarshaller);

			//Additions
			//AUTHOR 1
			Author author1 = new Author();
			author1.setFirstName("Igen");
			author1.setLastName("Jenő");
			author1.setAuthorID("23");
			// ha csak lekérdezéshez kell akkor inkább szedd ki az "adatbáz"
			
			//BOOK 1
			Book book1 = new Book();
			
			book1.setISBN("11773346");
			book1.setDateOfPublishing("2013");
			book1.setTitle("XML Tutorial");
			book1.setCopies(new Copies());
			book1.setAuthor(author1.getAuthorID());
			book1.setPublisher("2");
			
			addBook(root, book1);
			
			//BOOK 2
			
			Book book2 = new Book();
			
			book2.setISBN("11773347");
			book2.setDateOfPublishing("2011");
			book2.setTitle("JAXB Tutorial");
			book2.setCopies(new Copies());
			book2.setAuthor(author1.getAuthorID());
			book2.setPublisher("2");
			
			addBook(root, book2);
			//BOOK 3
			
			Book book3 = new Book();
			
			book3.setISBN("11773323");
			book3.setDateOfPublishing("1993");
			book3.setTitle("Cooking for Beginners");
			book3.setCopies(new Copies());
			book3.setAuthor("17");
			book3.setPublisher("2");
			
			Copies copiesContainer = new Copies();
			Copy copy1 = new Copy();
			Copy copy2 = new Copy();
			copy1.setAccessionNumber("12");
			copy1.setPrice(BigInteger.valueOf(500));
			copiesContainer.getCopy().add(copy1);
			copy2.setAccessionNumber("13");
			copy2.setPrice(BigInteger.valueOf(799));
			copy2.setBorrowed(null);
			copiesContainer.getCopy().add(copy2);
			book3.setCopies(copiesContainer);
			
			addBook(root, book3);
			
			//QUERY-COPY-1
			isBookHasCopy(root, book1);
			isBookHasCopy(root, book2);
			isBookHasCopy(root, book3);
			
			//MODIFICATIONS
			//MOD-COPY
			book1.setCopies(copiesContainer);
			updateBook(root, book1);
			
			System.out.println("Modification MOD-COPY has been set up");
			
			//QUERY-COPY-2
			isBookHasCopy(root, book1);
			
			//QUERY-PRICE-1
			System.out.println("Average price for [" + book3.getISBN() + "], " + book3.getTitle() + " is " + getCopiesAveragePriceForBook(root, book3));
			
			//MOD-PRICE
			updateCopyPricesForBook(root, book3, -62);
			System.out.println("Modification MOD-PRICE has been set up");
			
			//QUERY-PRICE-2
			System.out.println("Average price for [" + book3.getISBN() + "], " + book3.getTitle() + " after 62% discount is " + getCopiesAveragePriceForBook(root, book3));

			//QUERY-AUTHOR[23]-BOOKS-1
			System.out.println("Book titles for author '" 
				+ author1.getFirstName() + " " + author1.getLastName() 
				+ "' are " + getBookTitlesForAuthor(root, author1));
			
			//MOD-AUTHOR[23]
			deleteBook(root, book1);
			System.out.println("Modification MOD-AUTHOR[23] has been set up");
			
			//QUERY-AUTHOR[23]-BOOKS-2
			System.out.println("Book titles for author 'Igen Jenő' are " + getBookTitlesForAuthor(root, author1));
			
			saveXML(marshaller, root);

		} catch (JAXBException | SAXException | IOException e) {
			e.printStackTrace();
		}

	}

	private static Schema loadSchema() throws SAXException {
		SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
		Schema schema = schemaFactory.newSchema(new File(XML_SCHEMA_FILE));

		return schema;
	}

	private static Unmarshaller createUnmarshaller(Schema schema, JAXBContext jaxbContext) throws JAXBException {
		Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
		unmarshaller.setSchema(schema);

		return unmarshaller;
	}

	private static Marshaller createMarshaller(Schema schema, JAXBContext jaxbContext)
			throws JAXBException, PropertyException {
		Marshaller marshaller = jaxbContext.createMarshaller();
		marshaller.setSchema(schema);
		marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
		marshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");
		marshaller.setProperty(Marshaller.JAXB_NO_NAMESPACE_SCHEMA_LOCATION, XML_SCHEMA_FILE);

		return marshaller;
	}

	private static void resetXML() throws IOException {
		Files.copy(new File(XML_ORIGINAL_DOCUMENT_FILE).toPath(), new File(XML_DOCUMENT_FILE).toPath(),
				StandardCopyOption.REPLACE_EXISTING);
	}

	private static ROOT loadXML(Unmarshaller unmarshaller) throws JAXBException {
		File xmlFile = new File(XML_DOCUMENT_FILE);
		Source xmlSource = new StreamSource(xmlFile);

		JAXBElement<ROOT> jaxbElement = unmarshaller.unmarshal(xmlSource, ROOT.class);
		ROOT root = jaxbElement.getValue();

		return root;
	}

	private static void validateXML(Marshaller marshaller, ROOT root) throws JAXBException {
		// Validate object to schema
		marshaller.marshal(root, new DefaultHandler());
	}

	private static void saveXML(Marshaller marshaller, ROOT root) throws JAXBException {
		File xmlFile = new File(XML_DOCUMENT_FILE);

		validateXML(marshaller, root);
		marshaller.marshal(root, xmlFile);
	}

	private static void addBook(ROOT root, Book book) {
		if (root.getBooks().getBook().stream().filter(pr -> pr.getISBN() == book.getISBN())
				.count() > 0) {
			throw new IllegalArgumentException("Book with ISBN: " + book.getISBN() + " is already in database");
		} else {
			root.getBooks().getBook().add(book);
		}
	}

	private static void updateBook(ROOT root, Book book) {
		if (root.getBooks().getBook().stream().filter(pr -> pr.getISBN() == book.getISBN())
				.count() <= 0) {
			throw new IllegalArgumentException("Book with ISBN: " + book.getISBN() + " not found in database");
		} else {
			deleteBook(root, book);
			addBook(root, book);
		}
	}

	private static void deleteBook(ROOT root, Book book) {
		if (root.getBooks().getBook().stream().filter(pr -> pr.getISBN() == book.getISBN())
				.count() <= 0) {
			throw new IllegalArgumentException("Book with ISBN: " + book.getISBN() + " not found in database");
		} else {
			root.getBooks().getBook().removeIf(pr -> pr.getISBN() == book.getISBN());
		}
	}
	
	private static void isBookHasCopy(ROOT root, Book book) {
		List<String> accessionNumbers = getCopiesAsAccessionNumber(root, book);
		if(accessionNumbers.size() == 0) {
			System.out.println("Copy for book with ISBN: [" + book.getISBN() + "] was not found.");
		} else {
			System.out.println("Copy for book with ISBN: [" + book.getISBN() + "] was found with Accession Numbers: "  + accessionNumbers + ".");
		}
	}
	
	private static List<String> getCopiesAsAccessionNumber(ROOT root, Book book) {
		List<String> ANs = new ArrayList<String>();
		
		for (int i = 0; i < book.getCopies().getCopy().size(); i++) {
			ANs.add(book.getCopies().getCopy().get(i).getAccessionNumber());
		}
		
		return ANs;
	}
	
	private static double getCopiesAveragePriceForBook(ROOT root, Book book) {
		int count = book.getCopies().getCopy().size();
		if(count == 0) {
			return 0;
		} else {
			int sum = 0;
			for (int i = 0; i < count; i++) {
				sum += book.getCopies().getCopy().get(i).getPrice().intValue();
			}
			return (double)sum / count; 
		}
	}
	
	private static void updateCopyPricesForBook(ROOT root, Book book, double tax) {
		int count = book.getCopies().getCopy().size();
		if(count == 0) {
			throw new RuntimeException("No copy for this book has been found!");
		} else { 
			for (int i = 0; i < count; i++) {
				BigInteger newPrice = (BigInteger) BigDecimal.valueOf(Double.parseDouble(book.getCopies().getCopy().get(i).getPrice().toString()) * (1 + tax/100)).toBigInteger();
				book.getCopies().getCopy().get(i).setPrice(newPrice);
				updateBook(root, book);
			}
		}
	}
	
	private static List<String> getBookTitlesForAuthor(ROOT root, Author author) {
		List<String> bookList = new ArrayList<String>();
		for (int i = 0; i < root.getBooks().getBook().size(); i++) {
			if(root.getBooks().getBook().get(i).getAuthor() == author.getAuthorID()) {
				bookList.add(root.getBooks().getBook().get(i).getTitle());
			}
		}
		
		return bookList;
	}

}
