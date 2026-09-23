import {Activity,Landmark} from 'lucide-react'
import {PricingPanel} from './components/PricingPanel'
import {TransactionGrid} from './components/TransactionGrid'
export default function App(){return <><header><div className="brand"><span><Landmark size={22}/></span><div><strong>SRM</strong><small>CREDIT ENGINE</small></div></div><div className="environment"><Activity size={14}/><span/>Ambiente operacional</div></header><main><div className="hero"><div><p className="eyebrow">MESA DE CRÉDITO</p><h1>Operações multimoedas,<br/><em>precificadas com confiança.</em></h1></div><p>Simule, liquide e acompanhe recebíveis em um único fluxo operacional.</p></div><PricingPanel/><TransactionGrid/></main><footer>SRM Credit Engine <span>•</span> Dados financeiros protegidos e auditáveis</footer></>}
