@Repository
public interface ProdutoRepository extends JpaRepository<Produto, Long> {
    // Métodos de busca customizados podem vir aqui
    Optional<Produto> findByCodigoBarras(String codigoBarras);
}
