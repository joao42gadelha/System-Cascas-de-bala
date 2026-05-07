@Entity
@Table(name = "produtos")
public class Produto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String descricao;
    private Double valor;
    private String tipoProduto;
    private String codigoBarras;
    private String qrCode;

    // Getters e Setters
}
