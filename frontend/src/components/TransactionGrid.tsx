import {useState} from 'react'
import {useQuery} from '@tanstack/react-query'
import {ChevronLeft,ChevronRight,Filter,LoaderCircle,RefreshCw} from 'lucide-react'
import {getSettlements} from '../api/creditEngine'
import type {StatementFilters} from '../types'
import {dateTime,money} from '../utils/format'

const defaults:StatementFilters={from:'',to:'',currency:'',receivableType:'',page:0,size:10,sort:'createdAt,desc'}
export function TransactionGrid(){
  const [draft,setDraft]=useState(defaults);const [filters,setFilters]=useState(defaults);
  const query=useQuery({queryKey:['settlements',filters],queryFn:()=>getSettlements(filters),placeholderData:previous=>previous});const page=query.data;
  const apply=()=>setFilters({...draft,page:0});const clear=()=>{setDraft(defaults);setFilters(defaults)};const go=(next:number)=>setFilters(f=>({...f,page:next}));
  return <section className="card transactions">
    <div className="transactions-title"><div><p className="eyebrow">AUDITORIA</p><h2>Extrato de liquidações</h2><p className="subtitle">Histórico processado com paginação no servidor.</p></div><button className="icon-button" aria-label="Atualizar" onClick={()=>query.refetch()}><RefreshCw className={query.isFetching?'spin':''} size={18}/></button></div>
    <div className="filters">
      <label><span>De</span><input type="date" value={draft.from} onChange={e=>setDraft({...draft,from:e.target.value})}/></label>
      <label><span>Até</span><input type="date" value={draft.to} onChange={e=>setDraft({...draft,to:e.target.value})}/></label>
      <label className="grow"><span>Tipo</span><select value={draft.receivableType} onChange={e=>setDraft({...draft,receivableType:e.target.value as StatementFilters['receivableType']})}><option value="">Todos os tipos</option><option value="MERCANTILE_DUPLICATE">Duplicata mercantil</option><option value="POSTDATED_CHECK">Cheque pré-datado</option></select></label>
      <label><span>Moeda</span><select value={draft.currency} onChange={e=>setDraft({...draft,currency:e.target.value})}><option value="">Todas</option><option value="BRL">BRL</option><option value="USD">USD</option></select></label>
      <button className="filter-button" onClick={apply}><Filter size={16}/>Filtrar</button><button className="text-button" onClick={clear}>Limpar</button>
    </div>
    <div className="table-wrap"><table><thead><tr><th>Operação</th><th>Cedente</th><th>Tipo</th><th>Vencimento</th><th>Valor de face</th><th>Valor líquido</th><th>Status</th><th>Processada em</th></tr></thead><tbody>
      {query.isLoading&&<tr><td colSpan={8} className="empty"><LoaderCircle className="spin"/>Carregando transações...</td></tr>}
      {query.error&&<tr><td colSpan={8} className="empty error-text">{query.error.message}</td></tr>}
      {page?.content.map(item=><tr key={item.id}><td><code>{item.id.slice(0,8)}</code></td><td><span className="muted-id">{item.assignorId.slice(0,8)}…</span></td><td>{item.receivableType==='MERCANTILE_DUPLICATE'?'Duplicata':'Cheque'}</td><td>{new Date(`${item.dueDate}T12:00:00`).toLocaleDateString('pt-BR')}</td><td>{money(item.faceValue,item.assetCurrency)}</td><td className="strong-cell">{money(item.netAmount,item.settlementCurrency)}</td><td><span className="status-pill">Liquidada</span></td><td>{dateTime(item.createdAt)}</td></tr>)}
      {page&&!page.content.length&&<tr><td colSpan={8} className="empty">Nenhuma liquidação encontrada.</td></tr>}
    </tbody></table></div>
    <div className="pagination"><span>{page?`${page.totalElements} ${page.totalElements===1?'registro':'registros'}`:'—'}</span><div><button disabled={!page||page.first} onClick={()=>go(filters.page-1)}><ChevronLeft size={16}/></button><span>Página {(page?.number??0)+1} de {Math.max(page?.totalPages??1,1)}</span><button disabled={!page||page.last} onClick={()=>go(filters.page+1)}><ChevronRight size={16}/></button></div></div>
  </section>
}
