package service;

import org.example.dto.CreateBookRequest;
import org.example.exception.BookNotFoundException;
import org.example.mapper.BookMapper;
import org.example.model.Book;
import org.example.repository.BookRepository;
import org.example.service.BookService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BookServiceTest {

    @Mock
    private BookRepository repository;

    @Mock
    private BookMapper mapper;

    @InjectMocks
    private BookService service;

    @Test
    void getAll_returnsAllBooks() {
        // given
        Book book1 = Book.builder().id(1L).title("A").build();
        Book book2 = Book.builder().id(2L).title("B").build();
        when(repository.findAll()).thenReturn(List.of(book1, book2));

        // when
        List<Book> result = service.getAll();

        // then
        assertThat(result).hasSize(2).containsExactly(book1, book2);
        verify(repository).findAll();
    }

    @Test
    void getById_whenExists_returnsBook() {
        // given
        Book book = Book.builder().id(1L).title("A").build();
        when(repository.findById(1L)).thenReturn(Optional.of(book));

        // when
        Book result = service.getById(1L);

        // then
        assertThat(result.getTitle()).isEqualTo("A");
        verify(repository).findById(1L);
    }

    @Test
    void getById_whenMissing_throwsException() {
        // given
        when(repository.findById(1L)).thenReturn(Optional.empty());

        // when / then
        assertThatThrownBy(() -> service.getById(1L))
                .isInstanceOf(BookNotFoundException.class);
    }

    @Test
    void create_clearsIdAndSaves() {
        // given
        CreateBookRequest input = CreateBookRequest.builder().title("New").build();
        Book saved = Book.builder().id(1L).title("New").build();
        when(repository.save(saved)).thenReturn(saved);
        when(mapper.toEntity(input)).thenReturn(saved);

        // when
        Book result = service.create(input);

        // then
        assertThat(result).isEqualTo(saved);
        verify(repository).save(saved);
    }

    @Test
    void update_whenBookExists_updatesAndSaves() {
        // given
        Long id = 1L;
        Book existing = Book.builder()
                .id(id)
                .title("Old title")
                .author("Old author")
                .year(1990)
                .build();

        Book updated = Book.builder()
                .title("New title")
                .author("New author")
                .year(2024)
                .build();

        when(repository.findById(id)).thenReturn(Optional.of(existing));
        // Let save return the same entity instance to simplify assertions
        when(repository.save(ArgumentMatchers.any(Book.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        // when
        Book result = service.update(id, updated);

        // then
        assertThat(result.getId()).isEqualTo(id);
        assertThat(result.getTitle()).isEqualTo("New title");
        assertThat(result.getAuthor()).isEqualTo("New author");
        assertThat(result.getYear()).isEqualTo(2024);

        verify(repository).findById(id);
        verify(repository).save(existing);
    }

    @Test
    void delete_whenExists_deletesBook() {
        // given
        Long id = 1L;
        when(repository.existsById(id)).thenReturn(true);

        // when
        service.delete(id);

        // then
        verify(repository).existsById(id);
        verify(repository).deleteById(id);
    }

    @Test
    void delete_whenNotExists_throwsException() {
        // given
        when(repository.existsById(1L)).thenReturn(false);

        // when / then
        assertThatThrownBy(() -> service.delete(1L))
                .isInstanceOf(BookNotFoundException.class);
    }
}