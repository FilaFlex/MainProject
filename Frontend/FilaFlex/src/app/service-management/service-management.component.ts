import { Component } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { ServiceTableComponent } from '../service-table/service-table.component';
import { HttpClientModule } from '@angular/common/http';

@Component({
  selector: 'app-service-management',
  standalone: true,
  imports: [ServiceTableComponent, HttpClientModule],
  template: `
    <div class="p-6">
      <div class="flex justify-between items-center mb-4">
        <h1 class="text-2xl font-bold">Gerenciamento de Serviços</h1>
        <button (click)="handleAddService()" class="btn btn-primary">Adicionar Serviço</button>
      </div>
      
      <app-service-table 
        [services]="services" 
        (editService)="handleEditService($event)" 
        (deleteService)="handleDeleteService($event)">
      </app-service-table>
    </div>
  `,
  styles: [
    `
      .btn {
        padding: 8px 12px;
        border-radius: 5px;
        cursor: pointer;
      }
      .btn-primary {
        background-color: #007bff;
        color: white;
      }
    `
  ]
})
export class ServiceManagementComponent {
  services: any[] = [];

  private apiUrl = 'http://localhost:3000/services';

  constructor(private http: HttpClient) {}

  ngOnInit(): void {
    this.loadServices();
  }

  loadServices(): void {
    this.http.get<any[]>(this.apiUrl).subscribe(
      (data) => this.services = data,
      (error) => console.error('Erro ao carregar serviços:', error)
    );
  }

  // Método para adicionar serviço
  handleAddService(): void {
    const newService = {
      name: 'Novo Serviço',
      description: 'Descrição do serviço',
      category: 'Categoria',
      price: 100,
      executionTime: '1 hora',
      deliveryDate: '2025-03-20',
      documentation: 'Nenhuma'
    };

    this.http.post<any>(this.apiUrl, newService).subscribe(
      (data) => this.services = [...this.services, data],
      (error) => console.error('Erro ao adicionar serviço:', error)
    );
  }

  // Método para editar serviço
  handleEditService(id: number): void {
    const updatedService = {
      name: 'Serviço Atualizado',
      description: 'Nova descrição',
      category: 'Nova Categoria',
      price: 200,
      executionTime: '2 horas',
      deliveryDate: '2025-03-22',
      documentation: 'Documento atualizado'
    };

    this.http.put<any>(`${this.apiUrl}/${id}`, updatedService).subscribe(
      () => this.loadServices(),
      (error) => console.error('Erro ao editar serviço:', error)
    );
  }

  // Método para excluir serviço
  handleDeleteService(id: number): void {
    this.http.delete(`${this.apiUrl}/${id}`).subscribe(
      () => this.services = this.services.filter(service => service.id !== id),
      (error) => console.error('Erro ao excluir serviço:', error)
    );
  }
}
