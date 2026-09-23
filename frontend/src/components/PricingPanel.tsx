import {useEffect,useMemo,useState} from 'react'
import {useMutation,useQueryClient} from '@tanstack/react-query'
import {ArrowRight,Calculator,CheckCircle2,LoaderCircle,ShieldCheck} from 'lucide-react'
import {createSettlement,simulatePricing} from '../api/creditEngine'
import {useDebounce} from '../hooks/useDebounce'
import type {PricingRequest,ReceivableType} from '../types'
import {futureDate,money,percent} from '../utils/format'

const initial:PricingRequest={faceValue:10000,dueDate:futureDate(90),receivableType:'MERCANTILE_DUPLICATE',assetCurrency:'BRL',settlementCurrency:'BRL',baseRateMonthly:.01}
const uuid=()=>crypto.randomUUID()

export function PricingPanel(){
  const queryClient=useQueryClient();const [form,setForm]=useState(initial);const [assignorId,setAssignorId]=useState<string>(uuid);const [idempotencyKey,setKey]=useState<string>(uuid);const [notice,setNotice]=useState('');
  const debounced=useDebounce(form);const valid=useMemo(()=>form.faceValue>0&&!!form.dueDate&&form.assetCurrency.length===3&&form.settlementCurrency.length===3,[form]);
  const simulation=useMutation({mutationFn:simulatePricing});
  useEffect(()=>{if(valid)simulation.mutate({...debounced,assetCurrency:debounced.assetCurrency.toUpperCase(),settlementCurrency:debounced.settlementCurrency.toUpperCase()})},[debounced,valid]);
  const settlement=useMutation({mutationFn:createSettlement,onSuccess:data=>{setNotice(`Liquidação ${data.id.slice(0,8)} registrada com sucesso.`);setKey(uuid());queryClient.invalidateQueries({queryKey:['settlements']})}})
  const update=<K extends keyof PricingRequest>(key:K,value:PricingRequest[K])=>{setNotice('');setForm(current=>({...current,[key]:value}))}
  const submit=()=>settlement.mutate({idempotencyKey,assignorId,pricing:{...form,assetCurrency:form.assetCurrency.toUpperCase(),settlementCurrency:form.settlementCurrency.toUpperCase()}})
  const result=simulation.data;
  return <section className="workspace-grid">
    <div className="card form-card">
      <div className="section-heading"><span className="icon-box"><Calculator size={20}/></span><div><p className="eyebrow">NOVA OPERAÇÃO</p><h2>Simular recebível</h2></div></div>
      <div className="form-grid">
        <label className="field full"><span>Valor de face</span><div className="money-input"><b>R$</b><input type="number" min="0.01" step="0.01" value={form.faceValue} onChange={e=>update('faceValue',Number(e.target.value))}/></div></label>
        <label className="field"><span>Vencimento</span><input type="date" value={form.dueDate} onChange={e=>update('dueDate',e.target.value)}/></label>
        <label className="field"><span>Tipo de recebível</span><select value={form.receivableType} onChange={e=>update('receivableType',e.target.value as ReceivableType)}><option value="MERCANTILE_DUPLICATE">Duplicata mercantil</option><option value="POSTDATED_CHECK">Cheque pré-datado</option></select></label>
        <label className="field"><span>Moeda do ativo</span><input maxLength={3} value={form.assetCurrency} onChange={e=>update('assetCurrency',e.target.value.toUpperCase())}/></label>
        <label className="field"><span>Moeda de liquidação</span><input maxLength={3} value={form.settlementCurrency} onChange={e=>update('settlementCurrency',e.target.value.toUpperCase())}/></label>
        <label className="field full"><span>Taxa base mensal</span><div className="suffix-input"><input type="number" min="0" step="0.001" value={form.baseRateMonthly*100} onChange={e=>update('baseRateMonthly',Number(e.target.value)/100)}/><b>% a.m.</b></div></label>
      </div>
      <details className="advanced"><summary>Identificação da operação</summary><div className="form-grid compact"><label className="field full"><span>Identificador do cedente</span><input value={assignorId} onChange={e=>setAssignorId(e.target.value)}/></label><label className="field full"><span>Chave de idempotência</span><input value={idempotencyKey} onChange={e=>setKey(e.target.value)}/></label></div></details>
    </div>
    <div className="card result-card">
      <div className="live-status">{simulation.isPending?<><LoaderCircle className="spin" size={14}/> calculando</>:<><span/> simulação ao vivo</>}</div>
      <p className="eyebrow">VALOR LÍQUIDO ESTIMADO</p>
      <div className="net-value">{result?money(result.netAmount,form.settlementCurrency):'—'}</div>
      <div className="conversion"><span>{money(form.faceValue,form.assetCurrency)}</span><ArrowRight size={16}/><strong>{form.settlementCurrency}</strong></div>
      {simulation.error&&<div className="error-box">{simulation.error.message}</div>}
      <div className="metrics">
        <div><span>Prazo</span><strong>{result?.termDays??'—'} dias</strong></div><div><span>Spread</span><strong>{result?percent(result.spreadMonthly):'—'}</strong></div><div><span>Taxa de câmbio</span><strong>{result?.exchangeRate?.toFixed(6)??'—'}</strong></div><div><span>Valor presente</span><strong>{result?money(result.presentValue,form.assetCurrency):'—'}</strong></div>
      </div>
      <div className="security-note"><ShieldCheck size={17}/><span>Cálculo com precisão financeira e operação protegida por idempotência.</span></div>
      {notice&&<div className="success-box"><CheckCircle2 size={17}/>{notice}</div>}
      {settlement.error&&<div className="error-box">{settlement.error.message}</div>}
      <button className="primary-button" disabled={!result||settlement.isPending||!assignorId||!idempotencyKey} onClick={submit}>{settlement.isPending?<LoaderCircle className="spin" size={18}/>:<CheckCircle2 size={18}/>} Confirmar liquidação</button>
    </div>
  </section>
}
