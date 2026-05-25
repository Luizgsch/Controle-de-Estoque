export interface Produto {
  id?: number;
  nome: string;
  sku: string;
  categoria: string;
  preco: number;
  quantidade: number;
  quantidadeMinima: number;
  descricao?: string;
  dataCriacao?: string;
  dataAtualizacao?: string;
}
