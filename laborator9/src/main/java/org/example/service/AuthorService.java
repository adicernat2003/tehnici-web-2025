package org.example.service;

import lombok.RequiredArgsConstructor;
import org.example.exception.CriticalBusinessException;
import org.example.exception.NonCriticalException;
import org.example.model.Author;
import org.example.model.Book;
import org.example.repository.AuthorRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthorService {

    private final AuthorRepository authorRepository;

    /**
     * Read-only transaction:
     * - readOnly = true → optimization hint, no dirty writes expected.
     */
    @Transactional(readOnly = true)
    public List<Author> getAllAuthors() {
        return authorRepository.findAll();
    }

    @Transactional(rollbackForClassName = {"CriticalBusinessException"})
    public void riskyOperation(Long authorId, boolean critical) {
        Author author = authorRepository.findById(authorId)
                .orElseThrow(() -> new RuntimeException("Author not found"));

        author.setName(author.getName() + " (updated)");

        if (critical) {
            throw new CriticalBusinessException("Critical error: rollback!");
        } else {
            throw new NonCriticalException("Non-critical: no rollback.");
        }
    }

    /* propagation examples */

    /**
     * REQUIRED:
     * - Support a current transaction, create a new one if none exists.
     * - This is the default setting of a transaction annotation.
     */
    @Transactional(
            propagation = Propagation.REQUIRED,
            isolation = Isolation.READ_COMMITTED,
            timeout = 5,
            rollbackForClassName = {"CriticalBusinessException"},
            noRollbackForClassName = {"NonCriticalException"}
    )
    public Author createAuthorWithBooks(String authorName,
                                        List<String> bookTitles,
                                        boolean triggerError) {
        Author author = new Author();
        author.setName(authorName);

        for (String title : bookTitles) {
            Book book = new Book();
            book.setTitle(title);
            author.getBooks().add(book);
        }

        authorRepository.save(author);

        if (triggerError) {
            throw new NonCriticalException("Non-critical problem occurred, but we keep the transaction.");
        }

        return author;
    }

    /**
     * SUPPORTS:
     * - If a transaction exists, join it.
     * - If none exists, execute non-transactionally.
     */
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public List<Author> findAuthorsSupports() {
        // This might be inside a transaction (if caller has one) or not
        return authorRepository.findAll();
    }

    /**
     * MANDATORY:
     * - Requires an existing transaction.
     * - If none exists, throws IllegalTransactionStateException.
     */
    @Transactional(propagation = Propagation.MANDATORY)
    public Author updateAuthorNameMandatory(Long authorId, String newName) {
        Author author = authorRepository.findById(authorId)
                .orElseThrow(() -> new RuntimeException("Author not found"));
        author.setName(newName);

        return author;
    }

    /**
     * NEVER:
     * - Must NOT run inside a transaction.
     * - If a transaction exists, throws IllegalTransactionStateException.
     * Usually used for read-only operations where you explicitly want no tx.
     */
    @Transactional(propagation = Propagation.NEVER)
    public List<Author> findAuthorsNever() {
        return authorRepository.findAll();
    }

    /* isolation level examples */

    /**
     * DEFAULT:
     * - Use the database's default isolation (for PostgreSQL usually READ COMMITTED).
     */
    @Transactional(isolation = Isolation.DEFAULT)
    public List<Author> findAllAuthorsDefaultIsolation() {
        return authorRepository.findAll();
    }

    /**
     * READ_UNCOMMITTED (treated as READ COMMITTED in PostgreSQL):
     * - A constant indicating that dirty reads, non-repeatable reads, and phantom reads can occur.
     * This level allows a row changed by one transaction to be read by another transaction before any changes in that
     * row have been committed (a "dirty read"). If any of the changes are rolled back, the second transaction will
     * have retrieved an invalid row.
     * - In Postgres, effectively same as READ_COMMITTED.
     */
    @Transactional(isolation = Isolation.READ_UNCOMMITTED)
    public List<Author> findAllAuthorsReadUncommitted() {
        return authorRepository.findAll();
    }

    /**
     * READ_COMMITTED:
     * A constant indicating that dirty reads are prevented; non-repeatable reads and phantom reads can occur.
     * This level only prohibits a transaction from reading a row with uncommitted changes in it.
     */
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public List<Author> findAllAuthorsReadCommitted() {
        return authorRepository.findAll();
    }

    /**
     * REPEATABLE_READ:
     * - Ensures that rows read cannot change during the transaction (no non-repeatable reads).
     * - A constant indicating that dirty reads and non-repeatable reads are prevented; phantom reads can occur.
     * This level prohibits a transaction from reading a row with uncommitted changes in it, and it also prohibits the
     * situation where one transaction reads a row, a second transaction alters the row, and the first transaction
     * re-reads the row, getting different values the second time (a "non-repeatable read").
     */
    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public List<Author> findAllAuthorsRepeatableRead() {
        return authorRepository.findAll();
    }

    /**
     * SERIALIZABLE:
     * - Highest isolation level, behaves like transactions are fully serialized.
     * - A constant indicating that dirty reads, non-repeatable reads, and phantom reads are prevented.
     * This level includes the prohibitions in REPEATABLE_READ and further prohibits the situation where one transaction
     * reads all rows that satisfy a WHERE condition, a second transaction inserts a row that satisfies that WHERE condition,
     * and the first transaction re-reads for the same condition, retrieving the additional "phantom" row in the second read.
     */
    @Transactional(isolation = Isolation.SERIALIZABLE)
    public List<Author> findAllAuthorsSerializable() {
        return authorRepository.findAll();
    }

}