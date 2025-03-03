import { Component, Input, Output, EventEmitter } from '@angular/core';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-service-table',
  standalone: true,
  imports: [CommonModule], // ✅ Necessário para usar *ngFor
  template: `
    <table class="table-auto w-full border-collapse border border-gray-300">
      <thead>
        <tr class="bg-gray-100">
          <th class="border p-2">ID</th>
          <th class="border p-2">Nome</th>
          <th class="border p-2">Descrição</th>
          <th class="border p-2">Categoria</th>
          <th class="border p-2">Preço</th>
          <th class="border p-2">Tempo de Execução</th>
          <th class="border p-2">Data de Entrega</th>
          <th class="border p-2">Documentação</th>
          <th class="border p-2">Ações</th>
        </tr>
      </thead>
      <tbody>
        <tr *ngFor="let service of services" class="border">
          <td class="border p-2">{{ service.id }}</td>
          <td class="border p-2">{{ service.name }}</td>
          <td class="border p-2">{{ service.description }}</td>
          <td class="border p-2">{{ service.category }}</td>
          <td class="border p-2">{{ service.price | currency }}</td>
          <td class="border p-2">{{ service.executionTime }}</td>
          <td class="border p-2">{{ service.deliveryDate }}</td>
          <td class="border p-2">{{ service.documentation }}</td>
          <td class="border p-2 flex gap-2">
            <button (click)="editService.emit(service.id)" class="btn btn-outline">Editar</button>
            <button (click)="deleteService.emit(service.id)" class="btn btn-danger">Excluir</button>
          </td>
        </tr>
      </tbody>
    </table>
  `,
  styles: [
    `
      .btn {
        padding: 6px 10px;
        border-radius: 4px;
        cursor: pointer;
      }
      .btn-outline {
        border: 1px solid #007bff;
        color: #007bff;
        margin-right: 5px;
      }
      .btn-outline:hover {
        background-color: #007bff;
        color: white;
      }
      .btn-danger {
        background-color: #dc3545;
        color: white;
      }
      .btn-danger:hover {
        background-color: #c82333;
      }
    `
  ]
})
export class ServiceTableComponent {
  @Input() services: any[] = [];
  @Output() editService = new EventEmitter<number>();
  @Output() deleteService = new EventEmitter<number>();
}
