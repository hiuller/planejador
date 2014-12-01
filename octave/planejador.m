## Copyright (C) 2014 Usiminas
## 
## This program is free software; you can redistribute it and/or modify
## it under the terms of the GNU General Public License as published by
## the Free Software Foundation; either version 3 of the License, or
## (at your option) any later version.
## 
## This program is distributed in the hope that it will be useful,
## but WITHOUT ANY WARRANTY; without even the implied warranty of
## MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
## GNU General Public License for more details.
## 
## You should have received a copy of the GNU General Public License
## along with Octave; see the file COPYING.  If not, see
## <http://www.gnu.org/licenses/>.

## planejador

## Author: Usiminas <Usiminas@USIMINAS-HP>
## Created: 2014-11-03

    clear all;

    clc
    printf('\nUSIMINAS Planejador da Aciaria\n\n');
    
    %% dados do editor de parametros
      num_dias = 31;
      params = csvread('params.csv')(2:end,2);      
      plano  = csvread('plano.csv')(2:end,:);
      avail  = csvread('availability.csv');
      saldo_0  = params(21); printf('01) saldo de gusa inicial: %d t\n', saldo_0 );
      rend_ac1 = params(02); printf('02) rendimento aciaria 01: %.3f\n', rend_ac1);
      rend_ac2 = params(03); printf('03) rendimento aciaria 02: %.3f\n', rend_ac2);

    %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
    %% ENTRADAS DO MODELO
    %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
      prod_af = plano(1:31, 15); %% it doesn't matter from which BF the hotmetal comes, only the total is important            
      % hotmetal available after applying hot metal loss
      perda_gusa = params(1); printf('04) perda de gusa previs.: %.3f\n', perda_gusa);
      gusa_aciaria = (1.0 - perda_gusa)*prod_af;
      
      printf('05) hotmetal production: %d t\n', sum(prod_af) );
      clear('perda_gusa', 'prod_af');
return;
%% Steel shop #1          
      ncorr_ac1 = plano(:, 02);     % number of heats at ST.SHOP#1
      restr_ac1 = plano(:, 22);     % complex grades at ST.SHOP#1 - only one type
      trans_ac1 = plano(:, 06);     % trasfered heats from one shop to another
      
      comum_ac1 = (ncorr_ac1 .- restr_ac1) .- trans_ac1;
      p_ac1 = [comum_ac1 restr_ac1 trans_ac1];  % mild always comes first
    % standard metallic charge steel shop#1
      std_cm_ac1 = [ params(12) params(11) params(13) ]';
    % standard scrap ratio for steel shop#1
      std_su_ac1 = [ params(05) params(04) params(05) ]';
            
      cm_ac1 = (p_ac1*std_cm_ac1)./ncorr_ac1; % I have to deal with zero productions here
      cm_ac1( find( isnan(cm_ac1) ) ) = 0.0;
      su_ac1 = (p_ac1*std_su_ac1)./ncorr_ac1; % I have to deal with zero productions here
      su_ac1( find( isnan(su_ac1) ) ) = 0.0;

 %% Steel shop #2    *** SOLUÇÃO ***      
      restr_hic = plano(:, 16);
      restr_dr  = plano(:, 17);
      restr_rh  = plano(:, 18);
      restr_fp2 = plano(:, 19);
      ncorr_ac2 = zeros(num_dias, 1);
      
      for(i=1:num_dias)
        p = [ restr_hic(i)+restr_dr(i), restr_rh(i), restr_fp2(i) ];
        a = avail(i, :);
        ncorr_ac2(i, 1) = round( capacidade(a, p) );        
      end
      clear('p', 'a');
      % printf('Heat count: Octave versus Java\n');
      % display( [ncorr_ac2 plano(:,4)] ); % the heat count is influenced by the hotmetal equity control
      % pause();
      
      % using the Java heat count
      ncorr_ac2 = plano(:,4);
      
      comum_ac2 = ncorr_ac2 .- restr_hic .- restr_dr .- restr_fp2;
      p_ac2 = [restr_hic, restr_dr, restr_rh, restr_fp2, comum_ac2];
    % standard metallic charge steel shop#2
      std_cm_ac2 = [ params(14) params(15) params(16) params(17) params(18) ]';
    % standard scrap ratio for steel shop#2
      std_su_ac2 = [ params(06) params(07) params(08) params(09) params(10) ]';
            
      cm_ac2 = (p_ac2*std_cm_ac2)./sum(p_ac2')'; % I have to deal with zero productions here
      cm_ac2( find( isnan(cm_ac2) ) ) = 0.0;
      su_ac2 = (p_ac2*std_su_ac2)./sum(p_ac2')'; % I have to deal with zero productions here
      su_ac2( find( isnan(su_ac2) ) ) = 0.0;
      
      % printf('\nAvg. metallic charge: Octave versus Java\n');
      % display([ cm_ac2, plano(:,05) ]);
      % printf('\nScrap ratio: Octave versus Java\n');
      % display([ su_ac2, plano(:,10) ]);
      
    %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
    %% CALCULANDO O CONSUMO DE GUSA NA ACIARIA    
    %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%  
      consumo_gusa_ac1 = ((cm_ac1./rend_ac1).*(1.-su_ac1)).*sum(p_ac1')';
      consumo_gusa_ac2 = ((cm_ac2./rend_ac2).*(1.-su_ac2)).*sum(p_ac2')';
      consumo_gusa     = consumo_gusa_ac1 .+ consumo_gusa_ac2;
    %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
    %% CALCULANDO O SALDO DE GUSA
    %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%  
      saldo = zeros(num_dias, 1);
      saldo_ant = saldo_0;
      for(i=1:num_dias)
        saldo(i) = saldo_ant + gusa_aciaria(i) - consumo_gusa(i);
        saldo_ant = saldo(i);
      end
      clear('saldo_ant');
      
      printf('\nHotmetal equity calculation:\n');
      display([saldo]);
      
      
      
      