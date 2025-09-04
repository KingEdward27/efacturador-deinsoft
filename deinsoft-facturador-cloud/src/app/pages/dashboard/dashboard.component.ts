import { Component, OnInit } from '@angular/core';
import Chart from 'chart.js/auto';
import { AppService } from '@services/app.service';

@Component({
    selector: 'app-dashboard',
    templateUrl: './dashboard.component.html',
    styleUrls: ['./dashboard.component.scss']
})
export class DashboardComponent implements OnInit {

    public chart: any;
    public chart2: any;
    listData: any;
    cantidadContratos: any
    cantidadDeudores: any
    ratioEndeudamiento:any
    totalMontoPagos:any
    constructor(private appService: AppService) {
    }

    ngOnInit(): void {

        let cnfEmpresa = this.appService.getProfile().profile.split("|")[1];
        // this.actContratoService.getDashboard(cnfEmpresa).subscribe(data => {
        //     console.log(data);
        //     this.listData = data;
        //     this.cantidadContratos = data.cantidadContratos;
        //     this.cantidadDeudores = data.cantidadDeudores;
        //     this.ratioEndeudamiento = data.ratioEndeudamiento;
        //     this.totalMontoPagos = data.totalMontoPagos;
        // })
        // this.chart?.destroy()
        // this.chart2?.destroy()
        const NUMBER_CFG = {count: 12, min: 0, max: 2000};
        this.chart = new Chart("MyChart", {
            type: 'bar', //this denotes tha type of chart

            data: {// values on X-Axis
                labels: ['Enero', 'Febrero', 'Marzo', 'Abril',
                    'Mayo', 'Junio', 'Julio', 'Agosto', 'Setiembre', 'Octubre', 'Noviembre', 'Diciembre'],
                datasets: [
                    {
                        label: "Total adeudo",
                        data: ['467', '576', '572', '79', '92',
                            '574', '573', '576', '574', '573', '576', '576'],
                        backgroundColor: 'blue'
                    },
                    {
                        label: "Total recaudo",
                        data: ['542', '542', '536', '327', '17',
                            '0.00', '538', '541', '17',
                            '0.00', '538', '541'],
                        backgroundColor: 'limegreen'
                    }
                ]
            },
            options: {
                aspectRatio: 2,
                plugins: {
                    legend: {
                        position: 'bottom'
                    },
                    title: {
                        display: true,
                        text: 'Cumplimiento de pago de contratos'
                    }
                },
                scales: {
                    y: {
                        max: 20000,
                        min: 0,
                        ticks: {
                            stepSize: 0.5
                        }
                    }
                }
            }

        });
        this.chart2 = new Chart("MyChart2", {
            type: 'line', //this denotes tha type of chart
            data: {// values on X-Axis
                labels: ['Enero', 'Febrero', 'Marzo', 'Abril',
                    'Mayo', 'Junio', 'Julio', 'Agosto', 'Setiembre', 'Octubre', 'Noviembre', 'Diciembre'],
                datasets: [
                    {
                        label: "Sales",
                        data: ['467', '576', '572', '79', '92',
                            '574', '573', '576', '574', '573', '576', '576'],
                        backgroundColor: 'blue',
                        stack: 'combined',
                        type: 'bar'
                    },
                    {
                        label: "Profit",
                        data: ['542', '542', '536', '327', '17',
                            '0.00', '538', '541', '17',
                            '0.00', '538', '541'],
                        backgroundColor: 'limegreen',
                        stack: 'combined'
                    }
                ]
            },
            options: {
                aspectRatio: 2,
                plugins: {
                    legend: {
                        position: 'bottom'
                    },
                    title: {
                        display: true,
                        text: 'Chart.js Line Chart'
                    }
                }, scales: {
                    y: {
                        stacked: true
                    }
                }
            }

        });
    }

}
